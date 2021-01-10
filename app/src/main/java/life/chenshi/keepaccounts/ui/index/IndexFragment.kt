package life.chenshi.keepaccounts.ui.index

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.map
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.database.RecordType
import life.chenshi.keepaccounts.databinding.FragmentIndexBinding
import life.chenshi.keepaccounts.utils.DateUtil
import java.util.*

class IndexFragment : Fragment() {
    private lateinit var mBinding: FragmentIndexBinding
    private val mIndexViewModel by activityViewModels<IndexViewModel>()

    private var mAdapter: IndexRecordAdapter? = null

    companion object {
        private const val TAG = "IndexFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate<FragmentIndexBinding>(
            inflater, R.layout.fragment_index, container, false
        )
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        mBinding.rvBudget.layoutManager = LinearLayoutManager(activity)
        mAdapter = IndexRecordAdapter(Collections.emptyList())
        mBinding.rvBudget.adapter = mAdapter
    }

    private fun initListener() {
        // 时间
        mBinding.indexTime.setOnClickListener {
            it as TextView
            activity?.let { activity ->
                CardDatePickerDialog.builder(activity)
                    .setTitle("选择年月")
                    .showBackNow(false)
                    .setDisplayType(
                        mutableListOf(
                            DateTimeConfig.YEAR,//显示年
                            DateTimeConfig.MONTH,//显示月
                        )
                    )
                    .setMaxTime(System.currentTimeMillis())
                    .setThemeColor(Color.parseColor("#03A9F4"))
                    .setLabelText(year = "年", month = "月")
                    .setOnChoose { millisecond ->
                        mBinding.srlIndexRefresh.isRefreshing = true
                        mIndexViewModel.queryDateLiveData.value = millisecond
                    }
                    .build()
                    .show()
            }
        }

        // 新建记录
        mBinding.fabNewRecord.setOnClickListener {
            it.findNavController().navigate(R.id.action_indexFragment_to_newRecordActivity, null)
        }

        // 下拉刷新
        mBinding.srlIndexRefresh.setOnRefreshListener {
            mIndexViewModel.getRecordByDataRange()
        }
    }

    private fun initObserver() {
        // 首页加载日期范围内的数据
        mIndexViewModel.recordsByDateRangeLiveData
            .map { it ->
                mIndexViewModel.convert2RecordListGroupByDay(it)
            }.observe(viewLifecycleOwner) {
                mAdapter?.setData(it)
                if (mBinding.srlIndexRefresh.isRefreshing) {
                    mBinding.srlIndexRefresh.isRefreshing = false
                }
            }

        // 查询时间选择监听
        mIndexViewModel.queryDateLiveData.observe(viewLifecycleOwner) {
            val calendar = Calendar.getInstance().apply { timeInMillis = it }
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            mBinding.indexTime.text = "${year}年${month}月"
            val monthStart = DateUtil.getMonthStart(year, month)
            val monthEnd = DateUtil.getMonthEnd(year, month)
            mIndexViewModel.getRecordByDateRange(
                monthStart, monthEnd
            )
            mIndexViewModel.getSumMoneyByDateRange(monthStart, monthEnd)
                .observe(viewLifecycleOwner) { SumMoneyBeanList ->
                    // 防止只有收入/支出的时候,另外一个没有变化
                    mBinding.indexOutcomeInMonth.text = "-0.00"
                    mBinding.indexIncomeInMonth.text = "+0.00"
                    SumMoneyBeanList.forEach {
                        if (it.recordType == RecordType.OUTCOME) {
                            mBinding.indexOutcomeInMonth.text = "-${it.sumMoney}"
                        } else {
                            mBinding.indexIncomeInMonth.text = "+${it.sumMoney}"
                        }
                    }
                }
        }
    }
}