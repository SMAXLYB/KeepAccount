package life.chenshi.keepaccounts.ui.search

import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.base.BaseActivity
import life.chenshi.keepaccounts.databinding.ActivitySearchBinding
import life.chenshi.keepaccounts.ui.index.IndexRecordAdapter
import life.chenshi.keepaccounts.utils.ToastUtil
import life.chenshi.keepaccounts.utils.gone
import life.chenshi.keepaccounts.utils.inVisible
import life.chenshi.keepaccounts.utils.visible

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

        // 搜索
        mBinding.ivSearchSearch.setOnClickListener {
            val keyword = mBinding.etSearchKeyword.text
            if (!keyword.isNullOrBlank()) {
                mSearchViewModel.getRecordByKeyword(keyword.toString())
            } else {
                ToastUtil.showShort("请输入有效关键字")
                stopRefreshing()
            }
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

        mBinding.srlSearchRefresh.setOnRefreshListener {
            val keyword = mBinding.etSearchKeyword.text
            if (!keyword.isNullOrBlank()) {
                mSearchViewModel.getRecordByKeyword(keyword.toString())
            } else {
                ToastUtil.showShort("请输入有效关键字")
                stopRefreshing()
            }
        }
    }

    override fun initObserver() {
        mSearchViewModel.apply {

            recordsByKeywordLiveData.observe(this@SearchActivity) {
                mBinding.srlSearchRefresh.isRefreshing = true
                if (it == null || it.isEmpty()) {
                    showEmptyHintView()
                } else {
                    hideEmptyHintView()
                }
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
}