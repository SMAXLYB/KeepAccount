package life.chenshi.keepaccounts.ui.newrecord

import android.text.InputFilter
import android.text.Spanned
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jeremyliao.liveeventbus.LiveEventBus
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.common.base.BaseActivity
import life.chenshi.keepaccounts.common.base.BaseAdapter
import life.chenshi.keepaccounts.common.utils.*
import life.chenshi.keepaccounts.constant.BUSINESS
import life.chenshi.keepaccounts.constant.CATEGORY_TYPE_MAJOR
import life.chenshi.keepaccounts.constant.RECORD_TYPE_INCOME
import life.chenshi.keepaccounts.constant.RECORD_TYPE_OUTCOME
import life.chenshi.keepaccounts.database.entity.AbstractCategory
import life.chenshi.keepaccounts.database.entity.MinorCategory
import life.chenshi.keepaccounts.database.entity.Record
import life.chenshi.keepaccounts.databinding.ActivityNewRecordBinding
import life.chenshi.keepaccounts.databinding.BottomSheetRecyclerviewBinding
import life.chenshi.keepaccounts.databinding.BottomSheetRecyclerviewItemBinding
import life.chenshi.keepaccounts.ui.setting.category.CategoryActivity
import java.math.BigDecimal
import java.util.*

class NewRecordActivity : BaseActivity() {
    private val mRecordArgs by navArgs<NewRecordActivityArgs>()
    private val mBinding by bindingContentView<ActivityNewRecordBinding>(R.layout.activity_new_record)
    private val mNewRecordViewModel by viewModels<NewRecordViewModel>()
    private val mCommonCategoryAdapter by lazy { CommonCategoryAdapter(emptyList()) }

    companion object {
        private const val TAG = "NewRecordActivity"
    }

    override fun initView() {
        StatusBarUtil.init(this)
            .setColor(R.color.global_background_gray)
            .setDarkMode(true)

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
        mBinding.rvCategory.apply {
            layoutManager = LinearLayoutManager(this@NewRecordActivity, RecyclerView.HORIZONTAL, false)
            adapter = mCommonCategoryAdapter
        }
    }

    override fun initListener() {
        mBinding.bar.setLeftClickListener { onBackPressed() }
        mBinding.bar.setRightClickListener { }

        // 分类点击事件
        mCommonCategoryAdapter.setOnItemClickListener { _, category, _ ->
            if (mNewRecordViewModel.detailMode.value!!) {
                return@setOnItemClickListener
            }
            if (category.id == -1) {
                startActivity<CategoryActivity> {
                    putExtra(BUSINESS, "new_record")
                }
            } else {
                mNewRecordViewModel.currentAbstractCategory.value = category
            }
        }

        // 收支类型
        /* mBinding.tvRecordType.setOnClickListener {
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
        mBinding.tvMoney.filters = arrayOf(object : InputFilter {
            override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
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
        mBinding.tvBook.setOnClickListener {
            if (mNewRecordViewModel.detailMode.value!!) {
                return@setOnClickListener
            }
            lifecycleScope.launch(Dispatchers.IO) {
                mNewRecordViewModel.books
                    .take(1)
                    .collect {
                        // 前置条件,至少有一个账本
                        if (it.isEmpty()) {
                            ToastUtil.showShort(resources.getString(R.string.no_book_tip))
                            return@collect
                        }
                        // 弹窗显示
                        lifecycleScope.launch(Dispatchers.Main) {
                            showBottomSheetDialog(
                                it,
                                { binding, itemData ->
                                    binding.tvContent.text = itemData.name
                                },
                                { _, itemData, _ ->
                                    mNewRecordViewModel.currentBook.value = itemData
                                }
                            )
                        }
                    }
            }
        }

        // 日期
        mBinding.tvDatetime.setOnClickListener {
            if (mNewRecordViewModel.detailMode.value!!) {
                return@setOnClickListener
            }
            CardDatePickerDialog.builder(this)
                .setBackGroundModel(CardDatePickerDialog.STACK)
                .setThemeColor(getColorById(R.color.colorPrimary))
                .setDefaultTime(mNewRecordViewModel.currentDateTime.value!!)
                .showBackNow(false)
                .setDisplayType(DateTimeConfig.YEAR, DateTimeConfig.MONTH, DateTimeConfig.DAY, DateTimeConfig.HOUR, DateTimeConfig.MIN)
                .setLabelText("年", "月", "日", "时", "分")
                .setOnChoose {
                    mNewRecordViewModel.currentDateTime.value = it
                }
                .build()
                .show()
        }

        // 删除-长按
        mBinding.ivDelete.setOnLongClickListener {
            vibrate()
            mBinding.tvMoney.text = ""
            true
        }

        // 编辑按钮
        mBinding.mbtnEdit.setOnClickListener {
            // 取消详情模式
            mNewRecordViewModel.detailMode.value = false
        }
    }

    /**
     * 键盘点击事件
     */
    fun onKeyBoardClick(v: View) {
        when (val operation = v.tag as String) {
            "delete" -> {
                vibrate()
                mBinding.tvMoney.apply {
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
                mBinding.tvMoney.append(".")
            }
            else -> {
                mBinding.tvMoney.append(operation)
            }
        }
    }

    override fun initObserver() {
        mNewRecordViewModel.run {
            // 收支类型
            currentRecordType.observe(this@NewRecordActivity) {
                mBinding.tvRecordType.text = if (it == RECORD_TYPE_INCOME) {
                    mBinding.tvMoney.setTextColor(getColorById(R.color.income))
                    "收入"
                } else {
                    mBinding.tvMoney.setTextColor(getColorById(R.color.outcome))
                    "支出"
                }
                mBinding.tvMoney.isSelected = it == RECORD_TYPE_OUTCOME
            }

            // 账本
            currentBook.observe(this@NewRecordActivity) {
                mBinding.tvBook.text = it?.name ?: "未选择账本"
            }

            // 时间
            currentDateTime.observe(this@NewRecordActivity) {
                mBinding.tvDatetime.text = DateUtil.date2String(Date(it), DateUtil.YEAR_MONTH_DAY_HOUR_MIN_FORMAT)
            }

            // 常用类型
            commonMinorCategory.observe(this@NewRecordActivity) {
                mCommonCategoryAdapter.setData(it + MinorCategory(-1, "全部类型", recordType = 0, majorCategoryId = -1))
            }

            // 当前选中类型
            currentAbstractCategory.observe(this@NewRecordActivity) {
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
            detailMode.observe(this@NewRecordActivity) {
                if (it) {
                    // 如果是查看详情模式
                    setUpViewWhenDetail()
                    // bar 可见
                    mBinding.bar.visible()
                    mBinding.bar.setCenterTitle("账单详情")
                } else {
                    // 如果是新建或者进入进入编辑
                    mBinding.clKeyboard.visible()
                    // 如果有空间可见,否则不可见
                    mBinding.clKeyboard.post {
                        val diff = 70.dp2px()
                        if (mBinding.clKeyboard.top - mBinding.voucher.bottom < diff) {
                            mBinding.bar.gone()
                        } else {
                            mBinding.bar.visible()
                            if (!mNewRecordViewModel.record.isNull()) {
                                mBinding.bar.setCenterTitle("编辑账单")
                            }
                            mBinding.bar.hideRightIcon()
                        }
                    }
                    // 备注特殊处理
                    mBinding.etRemark.isEnabled = true
                }
            }
        }

        // 监听类型选中
        LiveEventBus.get("category", AbstractCategory::class.java)
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
                mBinding.mbtnEdit.visible()
                // 金额
                mBinding.tvMoney.text = String.format(it.money.toString())
                // 时间
                mNewRecordViewModel.currentDateTime.value = it.time.time
                // 备注
                mBinding.etRemark.setText(it.remark)
                mBinding.etRemark.isEnabled = false
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
        bindView: (BottomSheetRecyclerviewItemBinding, T) -> Unit,
        listener: (BottomSheetRecyclerviewItemBinding, T, Int) -> Unit,
    ) {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialog).apply {
            val binding = BottomSheetRecyclerviewBinding.inflate(layoutInflater).apply {
                rvContent.layoutManager = LinearLayoutManager(this@NewRecordActivity, RecyclerView.VERTICAL, false)
                rvContent.adapter =
                    object : BaseAdapter<T, BottomSheetRecyclerviewItemBinding>(
                        data
                    ) {
                        override fun getResLayoutId() = R.layout.bottom_sheet_recyclerview_item
                        override fun onBindViewHolder(
                            binding: BottomSheetRecyclerviewItemBinding,
                            itemData: T,
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
        finish()
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
            mBinding.bar.setCenterTitle("添加账单")
        } ?: insertRecord()

        // 清除数据
        clearData()
    }

    /**
     * 清除数据
     */
    private fun clearData() {
        mBinding.tvMoney.text = ""
        mBinding.etRemark.setText("", TextView.BufferType.EDITABLE)
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
        val money = BigDecimalUtil.yuan2FenBD(mBinding.tvMoney.text.toString())
        val remark = mBinding.etRemark.text.toString()
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
        val moneyText = mBinding.tvMoney.text
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
}