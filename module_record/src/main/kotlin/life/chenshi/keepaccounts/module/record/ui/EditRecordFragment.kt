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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
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
import life.chenshi.keepaccounts.module.common.utils.*
import life.chenshi.keepaccounts.module.common.view.CustomDialog
import life.chenshi.keepaccounts.module.record.R
import life.chenshi.keepaccounts.module.record.adapter.CommonCategoryAdapter
import life.chenshi.keepaccounts.module.record.databinding.RecordFragmentEditRecordBinding
import life.chenshi.keepaccounts.module.record.vm.IndexViewModel
import life.chenshi.keepaccounts.module.record.vm.NewRecordViewModel
import java.math.BigDecimal
import java.util.*

/**
 * 新建、查看、修改记录
 */
class EditRecordFragment : NavBindingFragment<RecordFragmentEditRecordBinding>() {
    private val mRecordArgs by navArgs<EditRecordFragmentArgs>()
    private val mNewRecordViewModel by viewModels<NewRecordViewModel>()
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
            mNewRecordViewModel.record = it
            mNewRecordViewModel.detailMode.value = true
        }

        // 默认账本
        mNewRecordViewModel.hasDefaultBook({
            lifecycleScope.launch {
                val book = mNewRecordViewModel.getBookById(it)
                mNewRecordViewModel.currentBook.value = book
            }
        }, {
            mNewRecordViewModel.currentBook.value = null
        })

        // 分类
        binding.rvCategory.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = mCommonCategoryAdapter
        }
    }

    override fun initListener() {
        binding.bar.setLeftClickListener { onBackPressed() }
        binding.bar.setRightClickListener {
            mNewRecordViewModel.confirmBeforeDelete {
                if (it) {
                    showDeleteDialog(mNewRecordViewModel.record!!)
                } else {
                    mIndexViewModel.deleteRecord(mNewRecordViewModel.record!!)
                    ToastUtil.showSuccess("删除成功")
                    onBackPressed()
                }
            }
        }

        // 分类点击事件
        mCommonCategoryAdapter.setOnItemClickListener { _, category, _ ->
            if (mNewRecordViewModel.detailMode.value!!) {
                return@setOnItemClickListener
            }
            if (category.id == -1) {
                ARouter.getInstance().navigation(ICategoryRouterService::class.java)
                    .navTo(requireContext()) {
                        putString(BUSINESS, this@EditRecordFragment.javaClass.name)
                    }
            } else {
                mNewRecordViewModel.currentAbstractCategory.value = category
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
        binding.tvBook.setOnClickListener {
            if (mNewRecordViewModel.detailMode.value!!) {
                return@setOnClickListener
            }
            lifecycleScope.launch(Dispatchers.IO) {
                mNewRecordViewModel.books
                    .take(1)
                    .collect {
                        // 前置条件,至少有一个账本
                        if (it.isEmpty()) {
                            ToastUtil.showShort(resources.getString(R.string.record_no_book_tip))
                            return@collect
                        }
                        // 弹窗显示
                        lifecycleScope.launch(Dispatchers.Main) {
                            showBottomSheetDialog(
                                it,
                                { binding, itemData -> binding.tvContent.text = itemData.name },
                                { _, itemData, _ -> mNewRecordViewModel.currentBook.value = itemData }
                            )
                        }
                    }
            }
        }

        // 日期
        binding.tvDatetime.setOnClickListener {
            if (mNewRecordViewModel.detailMode.value!!) {
                return@setOnClickListener
            }
            CardDatePickerDialog.builder(requireContext())
                .setBackGroundModel(CardDatePickerDialog.STACK)
                .setThemeColor(requireContext().getColorFromAttr(R.attr.colorPrimary))
                .setDefaultTime(mNewRecordViewModel.currentDateTime.value!!)
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
                    mNewRecordViewModel.currentDateTime.value = it
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
            mNewRecordViewModel.detailMode.value = false
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
        mNewRecordViewModel.run {
            // 收支类型
            currentRecordType.observe(this@EditRecordFragment) {
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
            currentBook.observe(this@EditRecordFragment) {
                binding.tvBook.text = it?.name ?: "未选择账本"
            }

            // 时间
            currentDateTime.observe(this@EditRecordFragment) {
                binding.tvDatetime.text =
                    DateUtil.date2String(Date(it), DateUtil.YEAR_MONTH_DAY_HOUR_MIN_FORMAT)
            }

            // 常用类型
            commonMinorCategory.observe(this@EditRecordFragment) {
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
            currentAbstractCategory.observe(this@EditRecordFragment) {
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
            detailMode.observe(this@EditRecordFragment) {
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
                            if (!mNewRecordViewModel.record.isNull()) {
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
        LiveEventBus.get(CATEGORY, AbstractCategory::class.java)
            .observe(this) { abstractCategory ->
                // 先更新集合
                mNewRecordViewModel.insertIfNotExistInCommonCategory(abstractCategory)
                // 后更新选中
                mNewRecordViewModel.currentAbstractCategory.value = abstractCategory
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
                binding.tvMoney.text = String.format(it.money.toString())
                // 时间
                mNewRecordViewModel.currentDateTime.value = it.time.time
                // 备注
                binding.etRemark.setText(it.remark)
                binding.etRemark.isEnabled = false
                // 账本
                mNewRecordViewModel.currentBook.value = mNewRecordViewModel.getBookById(it.bookId)
                // 类型 如果有子类,优先使用子类,否则使用主类
                val abstractCategory = if (it.minorCategoryId != 0) {
                    // 有可能子类被删除了
                    mNewRecordViewModel.getMinorCategoryById(it.minorCategoryId)
                } else {
                    // 有可能主类被删除了
                    mNewRecordViewModel.getMajorCategoryById(it.majorCategoryId)
                }
                // 如果被删除了,使用记录的状态
                if (abstractCategory.isNull()) {
                    mNewRecordViewModel.currentRecordType.value = it.recordType
                } else {
                    // 有TOP6可能并不包含,需要手动添加
                    mNewRecordViewModel.insertIfNotExistInCommonCategory(abstractCategory!!)
                    mNewRecordViewModel.currentAbstractCategory.value = abstractCategory
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
        val dialog = BottomSheetDialog(requireContext(), R.style.CommonBottomSheetDialog).apply {
            val binding = CommonBottomSheetRecyclerviewBinding.inflate(layoutInflater).apply {
                rvContent.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                rvContent.adapter =
                    object : BaseAdapter<T, CommonBottomSheetRecyclerviewItemBinding>(data) {
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
        mNewRecordViewModel.record?.apply { updateRecord(this) } ?: insertRecord()
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
        mNewRecordViewModel.record?.apply {
            updateRecord(this)
            mNewRecordViewModel.record = null
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
        mNewRecordViewModel.insertRecord(convertDataToRecord())
        ToastUtil.showSuccess("添加成功")
    }

    /**
     * 更新记录
     * @param record Record
     */
    private fun updateRecord(record: Record) {
        mNewRecordViewModel.updateRecord(convertDataToRecord(record))
        ToastUtil.showSuccess("更新成功")
    }

    /**
     * 将数据存储到record中
     * @param record Record?
     */
    private fun convertDataToRecord(record: Record? = null): Record {
        val money = BigDecimalUtil.yuan2FenBD(binding.tvMoney.text.toString())
        val remark = binding.etRemark.text.toString()
        val date = Date(mNewRecordViewModel.currentDateTime.value!!)
        val recordType = mNewRecordViewModel.currentRecordType.value!!
        val bookId = mNewRecordViewModel.currentBook.value!!.id!!

        mNewRecordViewModel.currentAbstractCategory.value!!.apply {
            var minorCategoryId = 0
            val majorCategoryId = if (this.categoryType == CATEGORY_TYPE_MAJOR) {
                this.id!!
            } else {
                this as MinorCategory
                minorCategoryId = this.id!!
                this.majorCategoryId
            }
            // 更新赋值
            record?.run {
                setAllData(money, remark, date, majorCategoryId, minorCategoryId, recordType, bookId)
                return this
            }
            //新建赋值
            return Record(null, money, remark, date, majorCategoryId, minorCategoryId, recordType, bookId)
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
        val money: BigDecimal = BigDecimalUtil.yuan2FenBD(moneyText.toString())
        if (money.compareTo(BigDecimal.ZERO) == 0) {
            ToastUtil.showShort("金额不能为0")
            return false
        }

        val book = mNewRecordViewModel.currentBook.value
        if (book == null) {
            ToastUtil.showShort("请选择账本")
            return false
        }

        if (mNewRecordViewModel.currentAbstractCategory.value.isNull()) {
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