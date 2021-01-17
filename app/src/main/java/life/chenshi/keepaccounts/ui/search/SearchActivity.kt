package life.chenshi.keepaccounts.ui.search

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.base.BaseActivity
import life.chenshi.keepaccounts.databinding.ActivitySearchBinding
import life.chenshi.keepaccounts.ui.index.IndexRecordAdapter
import life.chenshi.keepaccounts.utils.*

class SearchActivity : BaseActivity() {

    private val mBinding by bindingContentView<ActivitySearchBinding>(R.layout.activity_search)
    private val mSearchViewModel by viewModels<SearchViewModel>()
    private var mAdapter: IndexRecordAdapter? = null

    companion object {
        private const val TAG = "SearchActivity"

        const val FILTER_TYPE_ALL = -1
        const val FILTER_TYPE_OUTCOME = 0
        const val FILTER_TYPE_INCOME = 1

        const val ORDER_TYPE_DATE_ASC = 1000;
        const val ORDER_TYPE_DATE_DESC = 1001;
        const val ORDER_TYPE_MONEY_ASC = 1002;
        const val ORDER_TYPE_MONEY_DESC = 1003;
    }

    override fun initView() {
        mBinding.rvSearchRecords.layoutManager = LinearLayoutManager(this)
        mAdapter = IndexRecordAdapter(emptyList())
        mBinding.rvSearchRecords.adapter = mAdapter
    }

    override fun initListener() {

        // 返回键
        mBinding.ivSearchBack.setOnClickListener {
            finish()
        }

        // 关键字变化
        mBinding.etSearchKeyword.addTextChangedListener(
            afterTextChanged = {
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
        mBinding.etSearchKeyword.setOnEditorActionListener { v, actionId, event ->
            // 如果是回车搜索
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchingWithKeyword()
                return@setOnEditorActionListener true
            }

            false
        }

        // 搜索
        mBinding.ivSearchSearch.setOnClickListener {
            searchingWithKeyword()
        }

        // 日期筛选
        mBinding.tvSearchFilterDate.setOnClickListener {

        }

        // 类型筛选
        mBinding.tvSearchFilterType.setOnClickListener {
            val view =
                layoutInflater.inflate(R.layout.search_bottom_sheet_type, null) as ConstraintLayout
            val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
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
                            FILTER_TYPE_INCOME
                        }
                        R.id.ll_search_type_outcome -> {
                            FILTER_TYPE_OUTCOME
                        }
                        else -> {
                            FILTER_TYPE_ALL
                        }
                    }
                    bottomSheetDialog.dismiss()
                }
            }
        }

        // 金额筛选
        mBinding.tvSearchFilterMoney.setOnClickListener {}

        // 排序
        mBinding.tvSearchFilterOrder.setOnClickListener {
            val view =
                layoutInflater.inflate(R.layout.search_bottom_sheet_order, null) as ConstraintLayout
            val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
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
                                ORDER_TYPE_DATE_ASC
                            }
                            R.id.ll_search_order_date_desc -> {
                                ORDER_TYPE_DATE_DESC
                            }
                            R.id.ll_search_order_money_asc -> {
                                ORDER_TYPE_MONEY_ASC
                            }
                            else -> {
                                ORDER_TYPE_MONEY_DESC
                            }
                        }
                    bottomSheetDialog.dismiss()
                }
            }
        }

        // 下拉刷新
        mBinding.srlSearchRefresh.setOnRefreshListener {
            searchingWithKeyword()
        }
    }

    override fun initObserver() {
        mSearchViewModel.apply {
            // 类型变化
            filterType.observe(this@SearchActivity) {
                recordsByKeywordLiveData.apply {
                    value = value ?: emptyList()
                }
            }

            filterOrder.observe(this@SearchActivity) {
                recordsByKeywordLiveData.apply {
                    value = value ?: emptyList()
                }
            }

            // 数据变化
            recordsByKeywordLiveData.map {
                mBinding.srlSearchRefresh.isRefreshing = true
                convert2RecordListGroupByDay(it)
            }.observe(this@SearchActivity) {
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
        val keyword = mBinding.etSearchKeyword.text
        if (!keyword.isNullOrBlank()) {
            mSearchViewModel.getRecordByKeyword(keyword.toString())
        } else {
            ToastUtil.showShort("请输入有效关键字")
            stopRefreshing()
        }
    }
}