package life.chenshi.keepaccounts.ui.index

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.database.entity.Record
import life.chenshi.keepaccounts.database.entity.RecordType
import life.chenshi.keepaccounts.databinding.FragmentIndexBinding
import life.chenshi.keepaccounts.common.utils.DateUtil
import life.chenshi.keepaccounts.common.utils.ToastUtil
import life.chenshi.keepaccounts.common.utils.inVisible
import life.chenshi.keepaccounts.common.utils.visible
import java.util.*

class IndexFragment : Fragment() {
    private lateinit var mBinding: FragmentIndexBinding
    private val mIndexViewModel by activityViewModels<IndexViewModel>()

    private var mAdapter: IndexRecordAdapter? = null

    companion object {
        private const val TAG = "IndexFragment"
        const val SHOW_TYPE_ALL = -1;
        const val SHOW_TYPE_OUTCOME = 0;
        const val SHOW_TYPE_INCOME = 1;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(
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
        mAdapter = IndexRecordAdapter(emptyList())
        mAdapter!!.setOnClickListener { editRecord(it) }
        mAdapter!!.setOnItemLongClickListener { showDeleteDialog(it) }
        mBinding.rvBudget.adapter = mAdapter
    }


    private fun initListener() {
        // 时间
        mBinding.timeContainer.setOnClickListener {
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

        // 查看类型选择
        mBinding.typeContainer.setOnClickListener {
            activity?.let { activity ->
                val view =
                    layoutInflater.inflate(R.layout.index_bottom_sheet, null) as ConstraintLayout
                val bottomSheetDialog =
                    BottomSheetDialog(activity, R.style.BottomSheetDialog).apply {
                        setContentView(view)
                        setCanceledOnTouchOutside(true)
                        setCancelable(true)
                        show()
                    }
                val children = view.children
                children.forEach {
                    it.setOnClickListener { v ->
                        v as TextView
                        mBinding.tvCategory.text = v.text
                        mIndexViewModel.currentShowType.value =
                            when (v.id) {
                                R.id.ll_index_type_income -> {
                                    SHOW_TYPE_INCOME
                                }
                                R.id.ll_index_type_outcome -> {
                                    SHOW_TYPE_OUTCOME
                                }
                                else -> {
                                    SHOW_TYPE_ALL
                                }
                            }
                        bottomSheetDialog.dismiss()
                    }
                }
            }
        }

        // 新建记录
        mBinding.fabNewRecord.setOnClickListener {
            mIndexViewModel.hasDefaultBook({
                findNavController().navigate(R.id.action_indexFragment_to_newRecordActivity, null)
            }, {
                ToastUtil.showShort("当前尚未选择账本~")
            })
        }

        // 下拉刷新
        mBinding.srlIndexRefresh.setOnRefreshListener {
            mIndexViewModel.getRecordAndSumMoneyByDataRange()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initObserver() {
        // 首页加载日期范围内的数据
        mIndexViewModel.recordsByDateRangeLiveData
            .map {
                mBinding.srlIndexRefresh.isRefreshing = true
                mIndexViewModel.convert2RecordListGroupByDay(it)
            }.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    hideEmptyHintView()
                } else {
                    showEmptyHintView()
                }
                mAdapter?.setData(it)
                lifecycleScope.launch {
                    stopRefreshing()
                }
            }

        // 查询时间选择监听
        mIndexViewModel.queryDateLiveData.observe(viewLifecycleOwner) {
            val calendar = Calendar.getInstance().apply { timeInMillis = it }
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            mBinding.indexTime.text = "${year}年${month}月"
            val monthStart = DateUtil.getMonthStart(it)
            val monthEnd = DateUtil.getMonthEnd(it)
            mIndexViewModel.getRecordByDateRange(
                monthStart, monthEnd
            )
            mIndexViewModel.getSumMoneyByDateRange(monthStart, monthEnd)
                .observe(viewLifecycleOwner) { SumMoneyBeanList ->
                    // 防止只有收入/支出的时候,另外一个没有变化
                    mBinding.indexOutcomeInMonth.text = "-0.00"
                    mBinding.indexIncomeInMonth.text = "+0.00"
                    SumMoneyBeanList.forEach { bean ->
                        if (bean.recordType == RecordType.OUTCOME) {
                            mBinding.indexOutcomeInMonth.text = "-${bean.sumMoney}"
                        } else {
                            mBinding.indexIncomeInMonth.text = "+${bean.sumMoney}"
                        }
                    }
                }
        }

        // 收支类型选择
        mIndexViewModel.currentShowType.observe(viewLifecycleOwner) {
            mIndexViewModel.recordsByDateRangeLiveData.apply {
                if (value != null) {
                    value = value
                }
            }
        }

    }

    private fun editRecord(record: Record) {
        val action =
            IndexFragmentDirections.actionIndexFragmentToNewRecordActivity(record)
        findNavController().navigate(action)
    }

    private fun showDeleteDialog(record: Record) {
        activity?.let {
            AlertDialog.Builder(it)
                .setPositiveButton("确定") { _, _ -> mIndexViewModel.deleteRecord(record) }
                .setNegativeButton("取消") { _, _ -> }
                .setMessage("确定删除吗")
                .show()
        }
    }

    private fun stopRefreshing() {
        lifecycleScope.launch {
            if (mBinding.srlIndexRefresh.isRefreshing) {
                mBinding.srlIndexRefresh.isRefreshing = false
            }
        }
    }

    private fun hideEmptyHintView() {
        mBinding.ivIndexEmpty.inVisible()
        mBinding.tvIndexEmptyHint.inVisible()
    }

    private fun showEmptyHintView() {
        mBinding.ivIndexEmpty.visible()
        mBinding.tvIndexEmptyHint.visible()
    }
}