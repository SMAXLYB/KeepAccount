package life.chenshi.keepaccounts.ui.index

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.common.utils.*
import life.chenshi.keepaccounts.common.view.CustomDialog
import life.chenshi.keepaccounts.constant.SWITCHER_CONFIRM_BEFORE_DELETE
import life.chenshi.keepaccounts.database.entity.Record
import life.chenshi.keepaccounts.databinding.FragmentIndexBinding
import life.chenshi.keepaccounts.databinding.LayoutCustomPopwindowBinding

class IndexFragment : Fragment() {
    private lateinit var mBinding: FragmentIndexBinding
    private val behavior by lazy { BottomSheetBehavior.from(mBinding.drawer) }
    private val mIndexViewModel by activityViewModels<IndexViewModel>()
    private var mAdapter: IndexRecordAdapter? = null

    companion object {
        private const val TAG = "IndexFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = DataBindingUtil.inflate<FragmentIndexBinding>(
            inflater, R.layout.fragment_index, container, false
        )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        StatusBarUtil.init(requireActivity())
            .addPaddingTop(mBinding.bar)
        // 调整拖拽高度
        mBinding.hsv.post {
            val height = mBinding.rootView.bottom - mBinding.hsv.bottom - StatusBarUtil.getStatusBarHeight() - 50.dp2px()/*Tab栏高度*/
            behavior.peekHeight = height
        }
        mBinding.rvBudget.layoutManager = LinearLayoutManager(activity)
        mAdapter = IndexRecordAdapter(emptyList())
        mBinding.rvBudget.adapter = mAdapter
    }

    private fun initListener() {

        mAdapter!!.setOnClickListener { editRecord(it) }
        mAdapter!!.setOnItemLongClickListener { record ->
            if (mIndexViewModel.confirmBeforeDelete.value!!) {
                showDeleteDialog(record)
            } else {
                mIndexViewModel.deleteRecord(record)
            }
        }

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                mBinding.ivSearch.setVisibility(newState == BottomSheetBehavior.STATE_EXPANDED)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        // 新建记录
        mBinding.clNewRecord.setOnClickListener {
            mIndexViewModel.hasBook({
                findNavController().navigate(R.id.action_indexFragment_to_newRecordActivity, null)
            }, {
                ToastUtil.showShort(resources.getString(R.string.no_book_tip))
            })
        }

        // 搜索
        mBinding.clSearch.setOnClickListener {
            findNavController().navigate(R.id.action_indexFragment_to_searchFragment, null)

        }

        mBinding.ivSearch.setOnClickListener {
            findNavController().navigate(R.id.action_indexFragment_to_searchFragment, null)
        }

        // 查看类型
        mBinding.crbRecordType.setOnItemClickListener {
            mIndexViewModel.currentShowType.value = it
        }

        // 排序
        mBinding.tvSortBy.setOnClickListener {
            showPopupWindow(it)
        }
    }

    private fun initObserver() {
        mIndexViewModel.apply {
            // 首页加载日期范围内的数据
            recordsByDateRangeLiveData.observe(viewLifecycleOwner) {
                lifecycleScope.launch(Dispatchers.Main) {
                    val list = convert2RecordListGroupByDay(it)
                    if (list.isNotEmpty()) {
                        // hideEmptyHintView()
                    } else {
                        // showEmptyHintView()
                    }
                    mAdapter?.setData(list)
                }
            }

            // 收支类型选择
            currentShowType.observe(viewLifecycleOwner) {
                mIndexViewModel.recordsByDateRangeLiveData.apply {
                    if (value != null) {
                        value = value
                    }
                }
            }

            // 排序
            currentSortType.observe(viewLifecycleOwner) {
                mIndexViewModel.recordsByDateRangeLiveData.apply {
                    if (value != null) {
                        value = value
                    }
                }
            }

            // 删除确认
            lifecycleScope.launch {
                DataStoreUtil.readFromDataStore(SWITCHER_CONFIRM_BEFORE_DELETE, true).collect {
                    confirmBeforeDelete.value = it
                }
            }
        }
    }

    /**
     * 展示排序弹窗
     * @param it View
     */
    private fun showPopupWindow(it: View) {
        val options = arrayOf("时间正序", "时间倒序", "金额正序", "金额倒序")
        val binding = LayoutCustomPopwindowBinding.inflate(layoutInflater)
        binding.lvContent.adapter = ArrayAdapter(requireContext(), R.layout.popup_window_simple_list_item, options)
        PopupWindow(binding.root, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            setBackgroundDrawable(ColorDrawable())
            isOutsideTouchable = true
            binding.lvContent.setOnItemClickListener { _, _, position, _ ->
                mIndexViewModel.currentSortType.value = position
                dismiss()
            }
        }.showAsDropDown(it, -100, 10)
    }

    /**
     * 查看详细记录
     * @param record Record
     */
    private fun editRecord(record: Record) {
        val action =
            IndexFragmentDirections.actionIndexFragmentToNewRecordActivity(record)
        findNavController().navigate(action)
    }

    /**
     * 展示删除弹窗
     * @param record Record
     */
    private fun showDeleteDialog(record: Record) {
        activity?.let {
            CustomDialog.Builder(it)
                .setTitle("删除记录")
                .setPositiveButton("确定") { dialog, _ ->
                    mIndexViewModel.deleteRecord(record)
                    dialog.dismiss()
                }
                .setNegativeButton("取消")
                .setClosedButtonEnable(false)
                .setMessage("\u3000\u3000您正在进行删除操作, 此操作不可逆, 确定继续吗")
                .setCancelable(false)
                .build()
                .showNow()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding.unbind()
    }
}
/*
 @SuppressLint("SetTextI18n")
 private fun initObserver() {

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
                     if (bean.recordType == SHOW_TYPE_OUTCOME) {
                         mBinding.indexOutcomeInMonth.text = "-${bean.sumMoney}"
                     } else {
                         mBinding.indexIncomeInMonth.text = "+${bean.sumMoney}"
                     }
                 }
             }
     }

 private fun showTimePickerDialog() {
     activity?.let { activity ->
         CardDatePickerDialog.builder(activity)
             .showBackNow(false)
             .setDisplayType(
                 mutableListOf(
                     DateTimeConfig.YEAR,//显示年
                     DateTimeConfig.MONTH,//显示月
                 )
             )
             .setBackGroundModel(CardDatePickerDialog.STACK)
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


 */