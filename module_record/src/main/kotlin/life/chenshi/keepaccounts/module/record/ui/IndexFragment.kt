package life.chenshi.keepaccounts.module.record.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.library.view.guide.GuideView
import life.chenshi.keepaccounts.module.common.adapter.IndexRecordAdapter
import life.chenshi.keepaccounts.module.common.constant.*
import life.chenshi.keepaccounts.module.common.database.entity.Record
import life.chenshi.keepaccounts.module.common.service.ISettingRouterService
import life.chenshi.keepaccounts.module.common.utils.*
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper
import life.chenshi.keepaccounts.module.common.view.CustomDialog
import life.chenshi.keepaccounts.module.record.R
import life.chenshi.keepaccounts.module.record.databinding.RecordFragmentIndexBinding
import life.chenshi.keepaccounts.module.record.databinding.RecordLayoutCustomPopwindowBinding
import life.chenshi.keepaccounts.module.record.vm.IndexViewModel
import java.math.BigDecimal

@AndroidEntryPoint
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
            if (KVStoreHelper.read(APP_SHOW_USER_GUIDE, true)) {
                GuideView(requireContext())
                    .setRootView(requireActivity().window.decorView as FrameLayout)
                    .addGuideParameter {
                        setHighlight {
                            view = mBinding.clNewRecord
                            paddingHorizontal = 10f
                            paddingVertical = 10f
                        }
                        setTip {
                            viewId = R.layout.record_layout_guide_new_record
                        }
                    }
                    .addGuideParameter {
                        setHighlight {
                            view = mBinding.card
                            paddingHorizontal = 10f
                            paddingVertical = 10f
                        }
                        setTip {
                            viewId = R.layout.record_layout_guide_assets
                        }
                    }
                    .show()
                KVStoreHelper.write(APP_SHOW_USER_GUIDE, false)
            }
        }
        mBinding.rvBudget.layoutManager = LinearLayoutManager(activity)
        mAdapter = IndexRecordAdapter(emptyList())
        mBinding.rvBudget.adapter = mAdapter

        mBinding.lcAssetsChanges.apply {
            setScaleEnabled(false) //禁止缩放
            description.isEnabled = false // 去除说明
            setNoDataText("暂无数据")
            setNoDataTextColor(context.getColorFromAttr(R.attr.colorOnPrimary))
            setTouchEnabled(false)
            legend.isEnabled = false
        }

        mBinding.lcAssetsChanges.xAxis.isEnabled = false
        mBinding.lcAssetsChanges.axisLeft.isEnabled = false
        mBinding.lcAssetsChanges.axisRight.isEnabled = false
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

        mBinding.card.setOnClickListener {
            context?.navTo<ISettingRouterService>(Path(PATH_SETTING_ASSETS))
        }

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
            // td--使用arouter
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
                    mBinding.gpEmptyHint.setVisibility(list.isEmpty())
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

        launchAndRepeatWithViewLifecycle {
            launch {
                mIndexViewModel.recentRecords.collect {
                    mIndexViewModel.recentRecordsLiveData.value = it
                }
            }

            launch {
                mIndexViewModel.sumBalance
                    .distinctUntilChanged()
                    .collect {
                        mBinding.stvNetAssets.text = BigDecimalUtil.formatSeparator(it)
                    }
            }
            launch {
                mIndexViewModel.dailyBalanceByDateRange
                    .distinctUntilChanged()
                    .combine(mIndexViewModel.sumBalance) { daily, sum ->
                        var balance = sum
                        val emptyEntities = mIndexViewModel.generateEmptyEntriesOfMonth(
                            DateUtil.getDaysInMonth(System.currentTimeMillis())
                        )
                        val netAssetsDaily = daily.associateBy({ it.getDate() }, { it.getNetAssets() })

                        var negative = true // 假设全是负数, 全负数才需要反转
                        val size = emptyEntities.size - 1
                        for (i in size downTo 0) {
                            if (balance.compareTo(BigDecimal("0.00")) != -1) {
                                negative = false
                            }
                            emptyEntities[i].y = balance.toFloat()
                            balance = balance.subtract(netAssetsDaily[i + 1] ?: BigDecimal("0.00"))
                        }
                        // 如果金额都是负数就要倒过来显示
                        mBinding.lcAssetsChanges.axisLeft.isInverted = negative
                        emptyEntities
                    }
                    .collect {
                        val lineDataSet = LineDataSet(it, "总资产").apply {
                            axisDependency = YAxis.AxisDependency.LEFT // 依赖左轴
                            mode = LineDataSet.Mode.HORIZONTAL_BEZIER// 线段模式

                            setDrawFilled(true) //填充颜色
                            setDrawCircles(false)
                            setDrawValues(false)//不绘制值

                            fillDrawable = GradientDrawable(
                                GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(
                                    ColorUtils.setAlphaComponent(
                                        context?.getColorFromAttr(R.attr.colorPrimary) ?: 0, 50
                                    ),
                                    Color.parseColor("#01ffffff")
                                )
                            )
                            color = context?.getColorFromAttr(R.attr.colorPrimary) ?: 0 //线段颜色
                            getEntryForIndex(DateUtil.getDayInThisMonth() - 1).icon =
                                AppCompatResources.getDrawable(requireContext(), R.drawable.record_dot)

                            lineWidth = 2.5f
                        }

                        mBinding.lcAssetsChanges.apply {
                            data = LineData(lineDataSet) //填充数据
                            invalidate() //刷新
                        }
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