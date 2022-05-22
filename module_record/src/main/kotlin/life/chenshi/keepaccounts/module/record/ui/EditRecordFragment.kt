package life.chenshi.keepaccounts.module.record.ui

import android.text.InputFilter
import android.text.Spanned
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jeremyliao.liveeventbus.LiveEventBus
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.module.common.base.BaseAdapter
import life.chenshi.keepaccounts.module.common.base.NavBindingFragment
import life.chenshi.keepaccounts.module.common.constant.*
import life.chenshi.keepaccounts.module.common.database.entity.AbstractCategory
import life.chenshi.keepaccounts.module.common.database.entity.MinorCategory
import life.chenshi.keepaccounts.module.common.database.entity.Record
import life.chenshi.keepaccounts.module.common.databinding.CommonBottomSheetRecyclerviewBinding
import life.chenshi.keepaccounts.module.common.databinding.CommonBottomSheetRecyclerviewItemBinding
import life.chenshi.keepaccounts.module.common.service.ICategoryRouterService
import life.chenshi.keepaccounts.module.common.service.ISettingRouterService
import life.chenshi.keepaccounts.module.common.utils.*
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper
import life.chenshi.keepaccounts.module.common.view.CustomDialog
import life.chenshi.keepaccounts.module.record.R
import life.chenshi.keepaccounts.module.record.adapter.CommonCategoryAdapter
import life.chenshi.keepaccounts.module.record.databinding.RecordFragmentEditRecordBinding
import life.chenshi.keepaccounts.module.record.vm.EditRecordViewModel
import life.chenshi.keepaccounts.module.record.vm.IndexViewModel
import java.math.BigDecimal
import java.util.*

/**
 * 新建、查看、修改记录
 */
@AndroidEntryPoint
class EditRecordFragment : NavBindingFragment<RecordFragmentEditRecordBinding>() {
    private val mRecordArgs by navArgs<EditRecordFragmentArgs>()
    private val mEditRecordViewModel by viewModels<EditRecordViewModel>()
    private val mIndexViewModel by viewModels<IndexViewModel>()
    private val mCommonCategoryAdapter by lazy { CommonCategoryAdapter(emptyList()) }

    companion object {
        private const val TAG = "NewRecordActivity"
    }

    override fun setLayoutId(): Int = R.layout.record_fragment_edit_record

    override fun initView() {
        StatusBarUtil.init(requireActivity())
            .addStatusBatHeightTo(binding.bar)
        // 默认隐藏
        binding.bar.hideRightIcon()

        // record更新
        mRecordArgs.record?.let {
            mEditRecordViewModel.record = it
            mEditRecordViewModel.detailMode.value = true
            // 如果有账单, 先查账本里面的, 账本没有就不查了
            mEditRecordViewModel.getAssetsAccountById(it.assetsAccountId ?: -1L)
        } ?: kotlin.run {
            // 默认账本
            mEditRecordViewModel.hasDefaultBook({
                lifecycleScope.launch {
                    val book = mEditRecordViewModel.getBookById(it)
                    mEditRecordViewModel.currentBook.value = book
                }
            }, {
                mEditRecordViewModel.currentBook.value = null
            })

            // 如果没有账本, 直接查默认
            mEditRecordViewModel.getAssetsAccountById(KVStoreHelper.read(CURRENT_ASSET_ACCOUNT_ID, -1))
        }

        // 分类
        binding.rvCategory.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = mCommonCategoryAdapter
        }

    }

    override fun initListener() {
        binding.bar.setLeftClickListener { onBackPressed() }
        binding.bar.setRightClickListener {
            mEditRecordViewModel.confirmBeforeDelete {
                if (it) {
                    showDeleteDialog(mEditRecordViewModel.record!!)
                } else {
                    mIndexViewModel.deleteRecord(mEditRecordViewModel.record!!)
                    ToastUtil.showSuccess("删除成功")
                    onBackPressed()
                }
            }
        }

        // 分类点击事件
        mCommonCategoryAdapter.setOnItemClickListener { _, category, _ ->
            if (mEditRecordViewModel.detailMode.value!!) {
                return@setOnItemClickListener
            }
            if (category.id == -1) {
                ARouter.getInstance().navigation(ICategoryRouterService::class.java)
                    .navTo(requireContext()) {
                        putString(BUSINESS, this@EditRecordFragment.javaClass.name)
                    }
            } else {
                mEditRecordViewModel.currentAbstractCategory.value = category
            }
        }

        // 收支类型
        /* binding.tvRecordType.setOnClickListener {
             showBottomSheetDialog(
                 listOf("支出", "收入"),
                 { binding, itemData ->
                     binding.tvContent.text = itemData
                 },
                 { _, _, i ->
                     mNewRecordViewModel.currentRecordType.value = i
                 }
             )
         }*/

        // 资产账户
        binding.tvAsset.setNoDoubleClickListener {
            interval = 1000
            listener {
                if (mEditRecordViewModel.detailMode.value!!) {
                    return@listener
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    mEditRecordViewModel.assetAccounts
                        .take(1)
                        .flowOn(Dispatchers.IO)
                        .collect {
                            if (it.isEmpty()) {
                                ToastUtil.showShort("您还没有账户, 快去创建一个吧")
                                context?.navTo<ISettingRouterService>(Path(PATH_SETTING_ASSETS))
                                return@collect
                            }
                            showBottomSheetDialog(
                                data = it,
                                bindView = { binding, item ->
                                    if (mEditRecordViewModel.currentAssetsAccount.value?.id == item.id) {
                                        binding.ivSelected.visible()
                                    }
                                    binding.tvContent.text = item.name
                                },
                                listener = { _, item, _ ->
                                    mEditRecordViewModel.currentAssetsAccount.value = item
                                }
                            )
                        }
                }
            }
        }

        // 金额
        binding.tvMoney.filters = arrayOf(object : InputFilter {
            override fun filter(
                source: CharSequence?,
                start: Int,
                end: Int,
                dest: Spanned?,
                dstart: Int,
                dend: Int
            ): CharSequence? {
                source?.let { c ->
                    // 限制第一个不为"."
                    dest?.takeIf { (it.isEmpty() || it.contains(".")) && c == "." }?.run {
                        return ""
                    }

                    // 限制"."后只能有2位
                    dest?.takeIf { it.contains(".") }?.run {
                        val arr = this.split(".")
                        if (arr.size > 1) {
                            if (arr[1].length == 2) {
                                return ""
                            }
                        }
                    }

                    // 限制总体位数
                    dest?.takeIf { it.length == 8 && !it.contains(".") && c != "." }?.run {
                        return ""
                    }
                }
                return source
            }
        })

        // 账本
        binding.tvBook.setNoDoubleClickListener {
            interval = 1000
            listener {
                if (mEditRecordViewModel.detailMode.value!!) {
                    return@listener
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    mEditRecordViewModel.books
                        .take(1)
                        .flowOn(Dispatchers.IO)
                        .collect {
                            // 前置条件,至少有一个账本
                            if (it.isEmpty()) {
                                ToastUtil.showShort(resources.getString(R.string.record_no_book_tip))
                                return@collect
                            }
                            // 弹窗显示
                            showBottomSheetDialog(
                                it,
                                bindView = { binding, itemData ->
                                    binding.tvContent.text = itemData.name
                                    if (mEditRecordViewModel.currentBook.value?.id == itemData.id) {
                                        binding.ivSelected.visible()
                                    }
                                },
                                listener = { _, itemData, _ -> mEditRecordViewModel.currentBook.value = itemData }
                            )
                        }
                }
            }
        }

        // 日期
        binding.tvDatetime.setOnClickListener {
            if (mEditRecordViewModel.detailMode.value!!) {
                return@setOnClickListener
            }
            CardDatePickerDialog.builder(requireContext())
                .setBackGroundModel(CardDatePickerDialog.STACK)
                .setThemeColor(requireContext().getColorFromAttr(R.attr.colorPrimary))
                .setDefaultTime(mEditRecordViewModel.currentDateTime.value!!)
                .showBackNow(false)
                .setDisplayType(
                    DateTimeConfig.YEAR,
                    DateTimeConfig.MONTH,
                    DateTimeConfig.DAY,
                    DateTimeConfig.HOUR,
                    DateTimeConfig.MIN
                )
                .setLabelText("年", "月", "日", "时", "分")
                .setOnChoose {
                    mEditRecordViewModel.currentDateTime.value = it
                }
                .build()
                .show()
        }

        // 删除-长按
        binding.ivDelete.setOnLongClickListener {
            requireContext().vibrate()
            binding.tvMoney.text = ""
            true
        }

        // 编辑按钮
        binding.mbtnEdit.setOnClickListener {
            // 取消详情模式
            mEditRecordViewModel.detailMode.value = false
        }

        listOf(
            binding.tv1, binding.tv2, binding.tv3, binding.tv5, binding.tv6, binding.tv7,
            binding.tv9, binding.tv10, binding.tv11, binding.tv13, binding.tv14, binding.tvSave,
            binding.ivDelete, binding.tvCancel, binding.tvAppend
        ).forEach {
            it.setOnClickListener(this::onKeyBoardClick)
        }
    }

    /**
     * 键盘点击事件
     */
    private fun onKeyBoardClick(v: View) {
        when (val operation = v.tag as String) {
            "delete" -> {
                requireContext().vibrate()
                binding.tvMoney.apply {
                    if (!text.isNullOrBlank()) {
                        text = text.substring(0, text.length - 1)
                    }
                }
            }
            // "add" -> {
            //     "加"
            // }
            // "minus" -> {
            //     "减"
            // }
            "append" -> {
                append()
            }
            "save" -> {
                save()
            }
            "cancel" -> {
                onBackPressed()
            }
            "dot" -> {
                binding.tvMoney.append(".")
            }
            else -> {
                binding.tvMoney.append(operation)
            }
        }
    }

    override fun initObserver() {
        mEditRecordViewModel.run {
            // 收支类型
            currentRecordType.observe(viewLifecycleOwner) {
                binding.tvRecordType.text = if (it == RECORD_TYPE_INCOME) {
                    binding.tvMoney.setTextColor(requireContext().getColorFromRes(R.color.common_income))
                    "收入"
                } else {
                    binding.tvMoney.setTextColor(requireContext().getColorFromRes(R.color.common_outcome))
                    "支出"
                }
                binding.tvMoney.isSelected = it == RECORD_TYPE_OUTCOME
            }

            // 账本
            currentBook.observe(viewLifecycleOwner) {
                binding.tvBook.text = it?.name ?: "未选择账本"
            }

            // 账户
            currentAssetsAccount.observe(viewLifecycleOwner) {
                binding.tvAsset.text = it?.name ?: "不计入资产"
            }

            // 时间
            currentDateTime.observe(viewLifecycleOwner) {
                binding.tvDatetime.text =
                    DateUtil.date2String(Date(it), DateUtil.YEAR_MONTH_DAY_HOUR_MIN_FORMAT)
            }

            // 常用类型
            commonMinorCategory.observe(viewLifecycleOwner) {
                mCommonCategoryAdapter.setData(
                    it + MinorCategory(
                        -1,
                        "全部类型",
                        recordType = 0,
                        majorCategoryId = -1
                    )
                )
            }

            // 当前选中类型
            currentAbstractCategory.observe(viewLifecycleOwner) {
                // 如果有必要, 更新收支类型
                if (currentRecordType.value != it.recordType) {
                    currentRecordType.apply {
                        value = value!!.xor(1)
                    }
                }

                // 更新选中
                it?.let { abstractCategory ->
                    mCommonCategoryAdapter.apply {
                        setCurrentCategory(abstractCategory)
                    }
                }
            }

            // record更新
            detailMode.observe(viewLifecycleOwner) {
                if (it) {
                    // 如果是查看详情模式
                    setUpViewWhenDetail()
                    // bar 可见
                    binding.bar.visible()
                    binding.bar.showRightIcon()
                    binding.bar.setCenterTitle("账单详情")
                } else {
                    // 如果是新建或者进入进入编辑
                    binding.clKeyboard.visible()
                    // 如果有空间可见,否则不可见
                    binding.clKeyboard.post {
                        val diff = 50.dp2px()
                        if (binding.clKeyboard.top - binding.voucher.bottom < diff) {
                            // td--状态栏计算有问题
                            binding.bar.gone()
                        } else {
                            binding.bar.hideRightIcon()
                            binding.bar.visible()
                            if (!mEditRecordViewModel.record.isNull()) {
                                binding.bar.setCenterTitle("编辑账单")
                            }
                        }
                    }
                    // 备注特殊处理
                    binding.etRemark.isEnabled = true
                }
            }
        }

        // 监听类型选中
        LiveEventBus.get(CATEGORY, AbstractCategory::class.java).observe(viewLifecycleOwner) { abstractCategory ->
            // 先更新集合
            mEditRecordViewModel.insertIfNotExistInCommonCategory(abstractCategory)
            // 后更新选中
            mEditRecordViewModel.currentAbstractCategory.value = abstractCategory
        }
    }

    /**
     * 当入口是更新时,设置view
     */
    private fun setUpViewWhenDetail() {
        lifecycleScope.launch {
            // 更新记录
            mRecordArgs.record?.let {
                // 编辑按钮
                binding.mbtnEdit.visible()
                // 金额
                binding.tvMoney.text = it.money.toPlainString().removePrefix("-")
                // 时间
                mEditRecordViewModel.currentDateTime.value = it.time.time
                // 备注
                binding.etRemark.setText(it.remark)
                binding.etRemark.isEnabled = false
                // 账本
                mEditRecordViewModel.currentBook.value = mEditRecordViewModel.getBookById(it.bookId)
                // 类型 如果有子类,优先使用子类,否则使用主类
                val abstractCategory = if (it.minorCategoryId != 0) {
                    // 有可能子类被删除了
                    mEditRecordViewModel.getMinorCategoryById(it.minorCategoryId)
                } else {
                    // 有可能主类被删除了
                    mEditRecordViewModel.getMajorCategoryById(it.majorCategoryId)
                }
                // 如果被删除了,使用记录的状态
                if (abstractCategory.isNull()) {
                    mEditRecordViewModel.currentRecordType.value = it.recordType
                } else {
                    // 有TOP6可能并不包含,需要手动添加
                    mEditRecordViewModel.insertIfNotExistInCommonCategory(abstractCategory!!)
                    mEditRecordViewModel.currentAbstractCategory.value = abstractCategory
                }
            }
        }
    }

    /**
     * 展示底部对话框
     */
    private fun <T> showBottomSheetDialog(
        data: List<T>,
        bindView: (CommonBottomSheetRecyclerviewItemBinding, T) -> Unit,
        listener: (CommonBottomSheetRecyclerviewItemBinding, T, Int) -> Unit,
    ) {
        BottomSheetDialog(requireContext()).apply {
            val binding = CommonBottomSheetRecyclerviewBinding.inflate(layoutInflater).apply {
                rvContent.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                rvContent.adapter = object : BaseAdapter<T, CommonBottomSheetRecyclerviewItemBinding>(data) {
                    override fun setResLayoutId() =
                        R.layout.common_bottom_sheet_recyclerview_item

                    override fun onBindViewHolder(
                        binding: CommonBottomSheetRecyclerviewItemBinding, itemData: T,
                    ) {
                        bindView.invoke(binding, itemData)
                    }
                }.apply {
                    setOnItemClickListener { binding, itemData, i ->
                        listener.invoke(binding, itemData, i)
                        dismiss()
                    }
                }
            }
            setContentView(binding.root)
            show()
        }
    }

    /**
     * 保存 直接添加完成
     */
    private fun save() {
        if (!dataVerification()) {
            return
        }

        // 更新 或者 新建
        mEditRecordViewModel.record?.let { updateRecord() } ?: insertRecord()
        onBackPressed()
    }

    /**
     * 追记 添加完成后清除内容继续填写
     */
    private fun append() {
        if (!dataVerification()) {
            return
        }

        // 更新完一次后要置空
        // 更新 或者 新建
        mEditRecordViewModel.record?.let {
            updateRecord()
            mEditRecordViewModel.record = null
            binding.bar.setCenterTitle("添加账单")
        } ?: insertRecord()

        // 清除数据
        clearData()
    }

    /**
     * 清除数据
     */
    private fun clearData() {
        binding.tvMoney.text = ""
        binding.etRemark.setText("", TextView.BufferType.EDITABLE)
    }

    /**
     * 新建记录
     */
    private fun insertRecord() {
        mEditRecordViewModel.insertRecord(convertDataToRecord())
        ToastUtil.showSuccess("添加成功")
    }

    /**
     * 更新记录
     * @param oldRecord Record
     */
    private fun updateRecord() {
        mEditRecordViewModel.updateRecord(
            convertDataToRecord(mEditRecordViewModel.record), mEditRecordViewModel.record!!
        )
        ToastUtil.showSuccess("更新成功")
    }

    /**
     * 将数据存储到record中
     * @param record Record?
     */
    private fun convertDataToRecord(record: Record? = null): Record {
        val recordType = mEditRecordViewModel.currentRecordType.value!!
        val moneyText = if (recordType == RECORD_TYPE_OUTCOME) {
            "-${binding.tvMoney.text}"
        } else {
            "+${binding.tvMoney.text}"
        }
        val money = BigDecimalUtil.fromString(moneyText)
        val remark = binding.etRemark.text.toString()
        val date = Date(mEditRecordViewModel.currentDateTime.value!!)
        val bookId = mEditRecordViewModel.currentBook.value!!.id!!
        val assetsId = mEditRecordViewModel.currentAssetsAccount.value?.id

        mEditRecordViewModel.currentAbstractCategory.value!!.apply {
            var minorCategoryId = 0
            val majorCategoryId = if (this.categoryType == CATEGORY_TYPE_MAJOR) {
                this.id!!
            } else {
                this as MinorCategory
                minorCategoryId = this.id!!
                this.majorCategoryId
            }
            return Record(
                record?.id, money, remark, date, majorCategoryId, minorCategoryId, recordType, bookId, assetsId
            )
        }
    }

    /**
     * 数据校验
     */
    private fun dataVerification(): Boolean {
        val moneyText = binding.tvMoney.text
        if (moneyText.isNullOrEmpty()) {
            ToastUtil.showShort("请输入金额")
            return false
        }
        val money: BigDecimal = BigDecimalUtil.fromString(moneyText.toString())
        if (money.compareTo(BigDecimal.ZERO) == 0) {
            ToastUtil.showShort("金额不能为0")
            return false
        }

        val book = mEditRecordViewModel.currentBook.value
        if (book == null) {
            ToastUtil.showShort("请选择账本")
            return false
        }

        if (mEditRecordViewModel.currentAbstractCategory.value.isNull()) {
            ToastUtil.showShort("请选择类型")
            return false
        }

        return true
    }

    /**
     * 展示删除弹窗
     * @param record Record
     */
    private fun showDeleteDialog(record: Record) {
        CustomDialog.Builder(requireActivity())
            .setTitle("删除记录")
            .setPositiveButton("确定") { dialog, _ ->
                mIndexViewModel.deleteRecord(record)
                dialog.dismiss()
                ToastUtil.showSuccess("删除成功")
                onBackPressed()
            }
            .setNegativeButton("取消")
            .setClosedButtonEnable(false)
            .setMessage("\u3000\u3000您正在进行删除操作, 此操作不可逆, 确定继续吗")
            .setCancelable(false)
            .build()
            .showNow()
    }
}