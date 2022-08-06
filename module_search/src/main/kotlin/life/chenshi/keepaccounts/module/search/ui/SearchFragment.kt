package life.chenshi.keepaccounts.module.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.map
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import life.chenshi.keepaccounts.module.common.adapter.IndexRecordAdapter
import life.chenshi.keepaccounts.module.common.base.BaseFragment
import life.chenshi.keepaccounts.module.common.utils.*
import life.chenshi.keepaccounts.module.search.R
import life.chenshi.keepaccounts.module.search.adapter.FilterRecordDialogFragment
import life.chenshi.keepaccounts.module.search.databinding.SearchFragmentSearchBinding
import life.chenshi.keepaccounts.module.search.vm.SearchViewModel

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private lateinit var mBinding: SearchFragmentSearchBinding
    private val mSearchViewModel by viewModels<SearchViewModel>()
    private var mAdapter: IndexRecordAdapter? = null

    companion object {
        private const val TAG = "SearchFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.search_fragment_search, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
        initListener()
    }

    private fun initView() {
        mBinding.rvSearchRecords.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = IndexRecordAdapter(emptyList())
        mBinding.rvSearchRecords.adapter = mAdapter
    }

    private fun initListener() {

        // 返回键
        mBinding.ivSearchBack.setOnClickListener {
            onBackPressed()
        }

        // 关键字变化
        mBinding.etSearchKeyword.addTextChangedListener(
            afterTextChanged = {
                mSearchViewModel.keyword = it.toString()
                if (it.toString().isNotEmpty()) {
                    mBinding.imSearchClear.visible()
                } else {
                    mBinding.imSearchClear.gone()
                }
            }
        )

        // 清除搜索关键字
        mBinding.imSearchClear.setOnClickListener {
            mBinding.etSearchKeyword.setText("")
        }

        // 键盘回车
        mBinding.etSearchKeyword.setOnEditorActionListener { _, actionId, _ ->
            // 如果是回车搜索
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchingWithKeyword()
                return@setOnEditorActionListener true
            }

            false
        }

        // 筛选
        mBinding.ivSearchFilter.setOnClickListener {
            FilterRecordDialogFragment.newInstance().show(childFragmentManager, this::class.simpleName)
        }

        // 类型筛选
        /*mBinding.tvSearchFilterType.setNoDoubleClickListener {
            share = true
            listener {
                val view =
                    layoutInflater.inflate(R.layout.search_bottom_sheet_type, null) as ConstraintLayout
                val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.CommonBottomSheetDialog)
                    .apply {
                        setContentView(view)
                        setCancelable(true)
                        setCanceledOnTouchOutside(true)
                        show()
                    }
                val children = view.children
                children.forEach {
                    it.setOnClickListener { v ->
                        v as TextView
                        mBinding.tvSearchFilterType.text = v.text
                        mSearchViewModel.filterType.value = when (v.id) {
                            R.id.ll_search_type_income -> {
                                SHOW_TYPE_INCOME
                            }
                            R.id.ll_search_type_outcome -> {
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

        // 排序
        mBinding.tvSearchFilterOrder.setNoDoubleClickListener {
            share = true
            listener {
                val view =
                    layoutInflater.inflate(R.layout.search_bottom_sheet_order, null) as ConstraintLayout
                val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.CommonBottomSheetDialog)
                    .apply {
                        setContentView(view)
                        setCancelable(true)
                        setCanceledOnTouchOutside(true)
                        show()
                    }
                val children = view.children
                children.forEach {
                    it.setOnClickListener { v ->
                        v as TextView
                        mBinding.tvSearchFilterOrder.text = v.text
                        mSearchViewModel.filterOrder.value =
                            when (v.id) {
                                R.id.ll_search_order_date_asc -> {
                                    SORT_BY_DATE_ASC
                                }
                                R.id.ll_search_order_date_desc -> {
                                    SORT_BY_DATE_DESC
                                }
                                R.id.ll_search_order_money_asc -> {
                                    SORT_BY_MONEY_ASC
                                }
                                else -> {
                                    SORT_BY_MONEY_DESC
                                }
                            }
                        bottomSheetDialog.dismiss()
                    }
                }
            }
        }*/

        // 下拉刷新
        mBinding.srlSearchRefresh.setOnRefreshListener {
            searchingWithKeyword()
        }
    }

    private fun initObserver() {
        mSearchViewModel.apply {
            // 类型变化
            filterType.observe(viewLifecycleOwner) {
                recordsByKeywordLiveData.apply {
                    value = value ?: emptyList()
                }
            }

            filterOrder.observe(viewLifecycleOwner) {
                recordsByKeywordLiveData.apply {
                    value = value ?: emptyList()
                }
            }

            // 数据变化
            recordsByKeywordLiveData.map {
                mBinding.srlSearchRefresh.isRefreshing = true
                convert2RecordListGroupByDay(it)
            }.observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    showEmptyHintView()
                } else {
                    hideEmptyHintView()
                }
                mAdapter?.setData(it)
                stopRefreshing()
            }
        }
    }

    private fun showEmptyHintView() {
        mBinding.ivSearchNotFound.visible()
        mBinding.tvSearchNotFoundHint.visible()
    }

    private fun hideEmptyHintView() {
        mBinding.ivSearchNotFound.inVisible()
        mBinding.tvSearchNotFoundHint.inVisible()
    }

    private fun stopRefreshing() {
        mBinding.srlSearchRefresh.apply {
            if (this.isRefreshing) {
                this.isRefreshing = false
            }
        }
    }

    private fun searchingWithKeyword() {
        if (mBinding.etSearchKeyword.isSoftInputMethodVisible()) {
            mBinding.etSearchKeyword.hideSoftInputMethod()
        }
        if (mBinding.etSearchKeyword.hasFocus()) {
            mBinding.etSearchKeyword.clearFocus()
        }
        mSearchViewModel.searchRecord()
    }
}