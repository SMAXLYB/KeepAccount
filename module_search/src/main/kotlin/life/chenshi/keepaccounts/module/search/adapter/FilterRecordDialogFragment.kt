package life.chenshi.keepaccounts.module.search.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import life.chenshi.keepaccounts.module.common.constant.SortOption
import life.chenshi.keepaccounts.module.common.utils.DateUtil
import life.chenshi.keepaccounts.module.common.utils.getColorFromAttr
import life.chenshi.keepaccounts.module.common.utils.setNoDoubleClickListener
import life.chenshi.keepaccounts.module.search.R
import life.chenshi.keepaccounts.module.search.databinding.SearchDialogFilterRecordBinding
import life.chenshi.keepaccounts.module.search.vm.SearchViewModel
import java.util.*

@AndroidEntryPoint
class FilterRecordDialogFragment : BottomSheetDialogFragment() {

    private val mViewModel by viewModels<SearchViewModel>(ownerProducer = { requireParentFragment() })

    companion object {
        private const val TAG = "FilterRecordDialog"

        fun newInstance(): FilterRecordDialogFragment {
            val args = Bundle()
            val fragment = FilterRecordDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var mBinding: SearchDialogFilterRecordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = SearchDialogFilterRecordBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(false)
        initView()
        initObserver()
        initListener()
    }

    private fun initView() {
    }

    private fun initObserver() {
        mViewModel.startTime.observe(viewLifecycleOwner) {
            if (it == 0L) {
                mBinding.tvSearchTimeStart.text = "不限"
            } else {
                mBinding.tvSearchTimeStart.text =
                    DateUtil.date2String(Date(it), DateUtil.YEAR_MONTH_DAY_HOUR_MIN_FORMAT)
            }
        }

        mViewModel.endTime.observe(viewLifecycleOwner) {
            if (it == 0L) {
                mBinding.tvSearchTimeEnd.text = "不限"
            } else {
                mBinding.tvSearchTimeEnd.text = DateUtil.date2String(Date(it), DateUtil.YEAR_MONTH_DAY_HOUR_MIN_FORMAT)
            }
        }
    }

    private fun initListener() {
        mBinding.apply {

            btgSearchSortByMoney.addOnButtonCheckedListener { _, checkedId, isChecked ->
                changeSortOption(checkedId, isChecked)
            }

            btgSearchSortByTime.addOnButtonCheckedListener { _, checkedId, isChecked ->
                changeSortOption(checkedId, isChecked)
            }

            tvSearchTimeStart.setNoDoubleClickListener {
                listener {

                    val startTime = mViewModel.startTime.value ?: 0L
                    val endTime = mViewModel.endTime.value ?: 0L
                    val defaultTime = if (startTime == 0L) {
                        if (endTime != 0L && System.currentTimeMillis() > endTime) {
                            endTime
                        } else {
                            System.currentTimeMillis()
                        }
                    } else {
                        startTime
                    }

                    val builder = CardDatePickerDialog.builder(requireContext())
                        .setBackGroundModel(CardDatePickerDialog.STACK)
                        .setThemeColor(requireContext().getColorFromAttr(R.attr.colorPrimary))
                        .setDefaultTime(defaultTime)
                        .showBackNow(false)
                        .setDisplayType(
                            DateTimeConfig.YEAR,
                            DateTimeConfig.MONTH,
                            DateTimeConfig.DAY,
                            DateTimeConfig.HOUR,
                            DateTimeConfig.MIN
                        )
                        .setLabelText("年", "月", "日", "时", "分")
                        .setOnChoose {
                            mViewModel.startTime.value = it

                        }
                    if (endTime != 0L) {
                        builder.setMaxTime(endTime)
                    }
                    builder.build()
                        .show()
                }
            }

            tvSearchTimeEnd.setNoDoubleClickListener {
                listener {

                    val startTime = mViewModel.startTime.value ?: 0L
                    val endTime = mViewModel.endTime.value ?: 0L

                    val defaultTime = if (endTime == 0L) {
                        if (startTime != 0L && startTime > System.currentTimeMillis()) {
                            startTime
                        } else {
                            System.currentTimeMillis()
                        }
                    } else {
                        endTime
                    }

                    val builder = CardDatePickerDialog.builder(requireContext())
                        .setBackGroundModel(CardDatePickerDialog.STACK)
                        .setThemeColor(requireContext().getColorFromAttr(R.attr.colorPrimary))
                        .setDefaultTime(defaultTime)
                        .showBackNow(false)
                        .setDisplayType(
                            DateTimeConfig.YEAR,
                            DateTimeConfig.MONTH,
                            DateTimeConfig.DAY,
                            DateTimeConfig.HOUR,
                            DateTimeConfig.MIN
                        )
                        .setLabelText("年", "月", "日", "时", "分")
                        .setOnChoose {
                            mViewModel.endTime.value = it
                        }

                    if (startTime != 0L) {
                        builder.setMinTime(startTime)
                    }
                    builder.build()
                        .show()
                }
            }

            sliderSearchMoneyRange.addOnChangeListener { slider, value, fromUser ->
                // Log.d(TAG, "value=$value, fromUser=$fromUser, values=${slider.values}, valueFrom=${slider.valueFrom}, valueTo=${slider.valueTo}")
                etSearchMoneyStart.setText(slider.values.first().toString())
                etSearchMoneyEnd.setText(slider.values.last().toString())
            }

            cgSearchByAssetAccount.setOnCheckedStateChangeListener { group, checkedIds ->
                Log.d(TAG, "checkIds: $checkedIds")
            }

            cgSearchByBook.setOnCheckedStateChangeListener { group, checkedIds ->
                Log.d(TAG, "checkIds: $checkedIds")
            }

            cgSearchByCategory.setOnCheckedStateChangeListener { group, checkedIds ->
                Log.d(TAG, "checkIds: $checkedIds")
            }

            chip11.setOnCloseIconClickListener {
                Log.d(TAG, "closed")
                cgSearchByCategory.removeView(it)
            }

            cgSearchAssociateTo.setOnCheckedStateChangeListener { group, checkedIds ->
                Log.d(TAG, "checkIds: $checkedIds")
            }

            tvSearchCancel.setOnClickListener {
                dismiss()
            }
            tvSearchConfirm.setOnClickListener {
                dismiss()
                mViewModel.searchRecord()
            }
        }
    }

    private fun changeSortOption(checkedId: Int, isChecked: Boolean) {
        val option = when (checkedId) {
            R.id.btn_search_sort_by_money_desc -> SortOption.SortByMoneyDesc
            R.id.btn_search_sort_by_money_asc -> SortOption.SortByMoneyAsc
            R.id.btn_search_sort_by_time_desc -> SortOption.SortByTimeDesc
            R.id.btn_search_sort_by_time_asc -> SortOption.SortByTimeAsc
            else -> throw IllegalArgumentException("参数checkedId=${checkedId}不正确")
        }

        if (isChecked) {
            mViewModel.sortOptions.add(option)
        } else {
            mViewModel.sortOptions.remove(option)
        }

        Log.d(TAG, "changeSortOption: ${mViewModel.sortOptions}")
    }
}