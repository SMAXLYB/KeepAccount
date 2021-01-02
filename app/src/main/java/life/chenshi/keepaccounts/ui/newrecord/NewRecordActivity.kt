package life.chenshi.keepaccounts.ui.newrecord

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.database.Record
import life.chenshi.keepaccounts.databinding.ActivityNewRecordBinding
import life.chenshi.keepaccounts.utils.BigDecimalUtil
import life.chenshi.keepaccounts.utils.DateUtils
import java.math.BigDecimal
import java.util.*

class NewRecordActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityNewRecordBinding
    private val mNewRecordViewModel: NewRecordViewModel by lazy { ViewModelProvider(this)[NewRecordViewModel::class.java] }

    // 选择好的时间
    private val mCurrentChooseCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView<ActivityNewRecordBinding>(
            this,
            R.layout.activity_new_record
        )

        initView()
        initListener()
    }

    private fun initView() {
        mBinding.newRecordDate.text =
            DateUtils.date2String(mCurrentChooseCalendar.time, DateUtils.YEAR_MONTH_DAY_FORMAT)

        mBinding.newRecordTime.text =
            DateUtils.date2String(mCurrentChooseCalendar.time, DateUtils.HOUR_MINUTE)
    }

    private fun initListener() {
        // 日期选择
        mBinding.newRecordDate.setOnClickListener {
            val currentCalendar = Calendar.getInstance()
            val datePicker = DatePickerDialog.newInstance(
                { _, year, month, day ->
                    mCurrentChooseCalendar.apply {
                        set(year, month, day)
                    }
                    mBinding.newRecordDate.text =
                        DateUtils.date2String(
                            mCurrentChooseCalendar.time,
                            DateUtils.YEAR_MONTH_DAY_FORMAT
                        )
                },
                mCurrentChooseCalendar
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
                    mCurrentChooseCalendar.apply {
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minute)
                    }
                    mBinding.newRecordTime.text =
                        DateUtils.date2String(mCurrentChooseCalendar.time, DateUtils.HOUR_MINUTE)
                },
                mCurrentChooseCalendar.get(Calendar.HOUR_OF_DAY),
                mCurrentChooseCalendar.get(Calendar.MINUTE),
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

        //提交
        mBinding.btnSubmit.setOnClickListener {
            val money: BigDecimal = BigDecimalUtil.yuan2FenBD("12.34")
            val remark = mBinding.etRemark.text.toString()
            val date = mCurrentChooseCalendar.time

            mNewRecordViewModel.insertRecord(
                Record(null, money, remark, date, 1, 1)
            )
        }
    }
}