package life.chenshi.keepaccounts.module.record.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.module.common.adapter.IndexRecordAdapter
import life.chenshi.keepaccounts.module.common.constant.SWITCHER_CONFIRM_BEFORE_DELETE
import life.chenshi.keepaccounts.module.common.database.entity.Record
import life.chenshi.keepaccounts.module.common.utils.StatusBarUtil
import life.chenshi.keepaccounts.module.common.utils.ToastUtil
import life.chenshi.keepaccounts.module.common.utils.dp2px
import life.chenshi.keepaccounts.module.common.utils.setVisibility
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper
import life.chenshi.keepaccounts.module.common.view.CustomDialog
import life.chenshi.keepaccounts.module.record.R
import life.chenshi.keepaccounts.module.record.databinding.RecordFragmentIndexBinding
import life.chenshi.keepaccounts.module.record.databinding.RecordLayoutCustomPopwindowBinding
import life.chenshi.keepaccounts.module.record.vm.IndexViewModel

class IndexFragment : Fragment() {
    private var _binding: RecordFragmentIndexBinding? = null
    private val mBinding get() = _binding!!
    private var behavior: BottomSheetBehavior<ConstraintLayout>? = null
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
        _binding = DataBindingUtil.inflate(inflater, R.layout.record_fragment_index, container, false)
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
            .addStatusBatHeightTo(mBinding.bar)
        // 调整拖拽高度
        behavior = BottomSheetBehavior.from(mBinding.drawer)
        mBinding.hsv.post {
            val height =
                mBinding.rootView.bottom - mBinding.hsv.bottom - StatusBarUtil.getStatusBarHeight() - 50.dp2px()/*Tab栏高度*/
            behavior?.peekHeight = height
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

        behavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                mBinding.ivSearch.setVisibility(newState == BottomSheetBehavior.STATE_EXPANDED)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        // 新建记录
        mBinding.clNewRecord.setOnClickListener {
            mIndexViewModel.hasBook({
                val action = IndexFragmentDirections.actionIndexFragmentToMainActivity()
                findNavController().navigate(action)
            }, {
                ToastUtil.showShort(resources.getString(R.string.record_no_book_tip))
            })
        }

        // 搜索
        mBinding.clSearch.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri("keepAccounts://searchActivity".toUri())
                .build()
            findNavController().navigate(request)
        }

        mBinding.ivSearch.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri("keepAccounts://searchActivity".toUri())
                .build()
            findNavController().navigate(request)
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
            recentRecordsLiveData.observe(viewLifecycleOwner) {
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
                mIndexViewModel.recentRecordsLiveData.apply {
                    if (value != null) {
                        value = value
                    }
                }
            }

            // 排序
            currentSortType.observe(viewLifecycleOwner) {
                mIndexViewModel.recentRecordsLiveData.apply {
                    if (value != null) {
                        value = value
                    }
                }
            }

            // 删除确认
            KVStoreHelper.read(SWITCHER_CONFIRM_BEFORE_DELETE, true).apply {
                confirmBeforeDelete.value = this
            }
        }
    }

    /**
     * 展示排序弹窗
     * @param it View
     */
    private fun showPopupWindow(it: View) {
        val options = arrayOf("时间正序", "时间倒序", "金额正序", "金额倒序")
        val binding = RecordLayoutCustomPopwindowBinding.inflate(layoutInflater)
        binding.lvContent.adapter =
            ArrayAdapter(requireContext(), R.layout.record_popup_window_simple_list_item, options)
        PopupWindow(
            binding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
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
        val action = IndexFragmentDirections.actionIndexFragmentToMainActivity(record)
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
        _binding = null
        behavior = null
        mAdapter = null
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