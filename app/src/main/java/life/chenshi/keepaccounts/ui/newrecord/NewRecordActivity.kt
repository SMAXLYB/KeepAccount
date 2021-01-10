package life.chenshi.keepaccounts.ui.newrecord

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.base.BaseActivity
import life.chenshi.keepaccounts.database.Record
import life.chenshi.keepaccounts.database.RecordType
import life.chenshi.keepaccounts.databinding.ActivityNewRecordBinding
import life.chenshi.keepaccounts.utils.BigDecimalUtil
import life.chenshi.keepaccounts.utils.DateUtil
import life.chenshi.keepaccounts.utils.ToastUtil
import life.chenshi.keepaccounts.utils.setEnable
import java.math.BigDecimal
import java.util.*

class NewRecordActivity : BaseActivity() {
    private lateinit var mBinding: ActivityNewRecordBinding
    private val mNewRecordViewModel: NewRecordViewModel by viewModels<NewRecordViewModel>()

    companion object {
        private const val TAG = "NewRecordActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView<ActivityNewRecordBinding>(
            this,
            R.layout.activity_new_record
        )

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
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

    private fun initListener() {
        // 收支类型选择
        mBinding.newRecordTypeOutcome.setOnClickListener {
            it as TextView
            it.setEnable(false)
            mBinding.newRecordTypeIncome.setEnable(true)
            mBinding.newRecordLabelContainerOutcome.visibility = View.VISIBLE
            mBinding.newRecordLabelContainerIncome.visibility = View.GONE
        }
        mBinding.newRecordTypeIncome.setOnClickListener {
            it as TextView
            it.setEnable(false)
            mBinding.newRecordTypeOutcome.setEnable(true)
            mBinding.newRecordLabelContainerOutcome.visibility = View.GONE
            mBinding.newRecordLabelContainerIncome.visibility = View.VISIBLE
        }

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

            mNewRecordViewModel.insertRecord(
                Record(null, money, remark, date, category, recordType)
            )

            finish()
        }

        // 取消
        mBinding.btnCancel.setOnClickListener {
            finish()
        }
    }


    private fun initObserver() {

    }

}