package life.chenshi.keepaccounts.ui.index

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.databinding.FragmentIndexBinding
import life.chenshi.keepaccounts.utils.DateUtils
import java.util.*

class IndexFragment : Fragment() {
    private lateinit var mBinding: FragmentIndexBinding
    private val mIndexViewModel: IndexViewModel by lazy { ViewModelProvider(this)[IndexViewModel::class.java] }
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
        mBinding.time.setOnClickListener {
            activity?.let { activity ->
                CardDatePickerDialog.builder(activity)
                    .setTitle("选择年月")
                    .showBackNow(false)
                    .setDisplayType(
                        mutableListOf(
                            DateTimeConfig.YEAR,//显示年
                            DateTimeConfig.MONTH,//显示月)
                        )
                    )
                    .setThemeColor(Color.parseColor("#03A9F4"))
                    .setLabelText(year = "年", month = "月")
                    .setOnChoose { }
                    .setOnCancel { }
                    .build()
                    .show()
            }
        }

        // 新建记录
        mBinding.fabNewRecord.setOnClickListener {
            it.findNavController().navigate(R.id.action_indexFragment_to_newRecordActivity, null)
        }
    }

    private fun initObserver() {
        // 首页加载日期范围内的数据
        mIndexViewModel.getRecordByDateRange(
            DateUtils.getCurrentMonthStart(),
            DateUtils.getCurrentMonthEnd()
        )
            .map { it ->
                mIndexViewModel.convert2RecordListGroupByDay(it)
            }.observe(viewLifecycleOwner) {
                mAdapter?.setData(it)
            }
    }
}