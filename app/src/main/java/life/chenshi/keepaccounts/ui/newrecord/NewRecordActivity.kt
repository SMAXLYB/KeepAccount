package life.chenshi.keepaccounts.ui.newrecord

import android.text.InputFilter
import android.text.Spanned
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
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
import life.chenshi.keepaccounts.constant.RECORD_TYPE_INCOME
import life.chenshi.keepaccounts.constant.RECORD_TYPE_OUTCOME
import life.chenshi.keepaccounts.database.entity.MinorCategory
import life.chenshi.keepaccounts.database.entity.Record
import life.chenshi.keepaccounts.databinding.ActivityNewRecordBinding
import life.chenshi.keepaccounts.databinding.BottomSheetRecyclerviewBinding
import life.chenshi.keepaccounts.databinding.BottomSheetRecyclerviewItemBinding
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
        mCommonCategoryAdapter.setOnItemClickListener { binding, category, i ->
            if (category.id == -1) {

            }
        }

        // 收支类型
        mBinding.tvRecordType.setOnClickListener {
            showBottomSheetDialog(
                listOf("支出", "收入"),
                { binding, itemData ->
                    binding.tvContent.text = itemData
                },
                { _, _, i ->
                    mNewRecordViewModel.currentRecordType.value = i
                }
            )
        }

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
    /*

    override fun initView() {

        // 如果时从列表中进入,填充信息
        mRecordArgs.record?.let {
            mNewRecordViewModel.mCurrentChooseCalendar.time = it.time
            mBinding.etMoney.setText(it.money.toString())
            mBinding.etRemark.setText(it.remark)
        }

        // 日期
        mBinding.newRecordDate.text =
            DateUtil.date2String(
                mNewRecordViewModel.mCurrentChooseCalendar.time,
                DateUtil.YEAR_MONTH_DAY_FORMAT
            )

        mBinding.newRecordTime.text =
            DateUtil.date2String(
                mNewRecordViewModel.mCurrentChooseCalendar.time,
                DateUtil.HOUR_MINUTE
            )

        var categoryIndex = 0
        mBinding.newRecordLabelContainerOutcome.children.iterator()
            .forEach {
                it.tag = categoryIndex++;
                mNewRecordViewModel.categoryViews.add(it)
            }
        mBinding.newRecordLabelContainerIncome.children.iterator()
            .forEach {
                it.tag = categoryIndex++;
                mNewRecordViewModel.categoryViews.add(it)
            }
        mNewRecordViewModel.categoryViews.forEach {
            if (it.tag as Int == mNewRecordViewModel.lastSelectedCategoryIndex) {
                it as TextView
                it.setEnable(false)
            }
        }
    }

    override fun initListener() {

        // 日期选择
        mBinding.newRecordDate.setOnClickListener {
            val currentCalendar = Calendar.getInstance()
            val datePicker = DatePickerDialog.newInstance(
                { _, year, month, day ->
                    mNewRecordViewModel.mCurrentChooseCalendar.apply {
                        set(year, month, day)
                    }
                    mBinding.newRecordDate.text =
                        DateUtil.date2String(
                            mNewRecordViewModel.mCurrentChooseCalendar.time,
                            DateUtil.YEAR_MONTH_DAY_FORMAT
                        )
                },
                mNewRecordViewModel.mCurrentChooseCalendar
            ).apply {
                accentColor = Color.parseColor("#03A9F4")
                maxDate = currentCalendar
                show(supportFragmentManager, "DatePicker")
            }
        }

        // 时间选择
        mBinding.newRecordTime.setOnClickListener {
            val currentCalendar = Calendar.getInstance()
            val timePicker = TimePickerDialog.newInstance(
                { _, hour, minute, _ ->
                    mNewRecordViewModel.mCurrentChooseCalendar.apply {
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minute)
                    }
                    mBinding.newRecordTime.text =
                        DateUtil.date2String(
                            mNewRecordViewModel.mCurrentChooseCalendar.time,
                            DateUtil.HOUR_MINUTE
                        )
                },
                mNewRecordViewModel.mCurrentChooseCalendar.get(Calendar.HOUR_OF_DAY),
                mNewRecordViewModel.mCurrentChooseCalendar.get(Calendar.MINUTE),
                true
            ).apply {
                accentColor = Color.parseColor("#03A9F4")
                setMaxTime(
                    currentCalendar.get(Calendar.HOUR_OF_DAY),
                    currentCalendar.get(Calendar.MINUTE),
                    1
                )
                show(supportFragmentManager, "TimePicker")
            }

        }

        // 主题选择
        val listener = View.OnClickListener {
            // 先将自身置为高亮状态
            it as TextView
            it.setEnable(false)

            //将上一次选择的类型标记为普通状态
            mNewRecordViewModel.categoryViews.forEachIndexed { index, view ->
                // 如果是上一次选择的类型
                if (index == mNewRecordViewModel.lastSelectedCategoryIndex) {
                    view as TextView
                    view.setEnable(true)
                }
            }

            // 记录当前选择的类型
            mNewRecordViewModel.lastSelectedCategoryIndex = it.tag as Int
        }
        mNewRecordViewModel.categoryViews.forEach { it.setOnClickListener(listener) }

        // 提交
        mBinding.btnSubmit.setOnClickListener {
            if (mBinding.etMoney.text.isNullOrEmpty()) {
                ToastUtil.showShort("请填写金额")
                return@setOnClickListener
            }
            val money: BigDecimal = BigDecimalUtil.yuan2FenBD(mBinding.etMoney.text.toString())
            val remark = mBinding.etRemark.text.toString()
            val date = mNewRecordViewModel.mCurrentChooseCalendar.time
            val recordType = if (!mBinding.newRecordTypeOutcome.isEnabled) {
                RecordType.OUTCOME
            } else {
                RecordType.INCOME
            }
            val category = mNewRecordViewModel.lastSelectedCategoryIndex

            mRecordArgs.record?.let {
                it.money = money
                it.remark = remark
                it.time = date
                it.category = category
                it.recordType = recordType
                mNewRecordViewModel.updateRecord(it)
                finish()
                return@setOnClickListener
            }

            mNewRecordViewModel.hasDefaultBook({
                mNewRecordViewModel.insertRecord(
                    Record(null, money, remark, date, category,0, recordType, it)
                )
                finish()
            }, {
                ToastUtil.showShort("您还没有账本,快去新建一个吧~")
            })
        }
    }
    */

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

            commonMinorCategory.observe(this@NewRecordActivity) {
                mCommonCategoryAdapter.setData(it + MinorCategory(-1, "全部类型", recordType = 0, majorCategoryId = -1))
            }
        }
    }

    /**
     * 展示底部对话框
     */
    private fun <T> showBottomSheetDialog(
        data: List<T>,
        bindView: (BottomSheetRecyclerviewItemBinding, T) -> Unit,
        listener: (BottomSheetRecyclerviewItemBinding, T, Int) -> Unit
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
                            itemData: T
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

        val money = BigDecimalUtil.yuan2FenBD(mBinding.tvMoney.text.toString())
        val remark = mBinding.etRemark.text.toString()
        val date = Date(mNewRecordViewModel.currentDateTime.value!!)
        val recordType = mNewRecordViewModel.currentRecordType.value!!
        val bookId = mNewRecordViewModel.currentBook.value!!.id!!
        mNewRecordViewModel.insertRecord(
            Record(null, money, remark, date, 0, 0, recordType, bookId)
        )
        ToastUtil.showSuccess("添加成功")
        finish()
    }

    /**
     * 追记 添加完成后清除内容继续填写
     */
    private fun append() {
        if (!dataVerification()) {
            return
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

        return true
    }
}