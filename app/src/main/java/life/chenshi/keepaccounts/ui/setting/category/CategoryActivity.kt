package life.chenshi.keepaccounts.ui.setting.category

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.common.base.BaseActivity
import life.chenshi.keepaccounts.common.utils.StatusBarUtil
import life.chenshi.keepaccounts.databinding.LayoutCategoryBinding

class CategoryActivity : BaseActivity() {
    companion object {
        private const val TAG = "CategoryActivity"
    }

    private val mCategoryViewModel by viewModels<CategoryViewModel>()
    private val mBinding by bindingContentView<LayoutCategoryBinding>(R.layout.layout_category)

    override fun initView() {
        StatusBarUtil.init(this)
            .setColor(R.color.white)
            .setDarkMode(true)

        mBinding.pager.adapter = CategoryPagerAdapter(this)
        TabLayoutMediator(mBinding.tab, mBinding.pager) { tab, position ->
            // val view = layoutInflater.inflate(R.layout.item_category_tab, null)
            // view.findViewById<ImageView>(R.id.iv_tab)
            // view.findViewById<TextView>(R.id.tv_tab)
            // tab.customView = view
            tab.text = if(position == 0){
                "支出"
            }else{
                "收入"
            }
        }.attach()
    }

    override fun initListener() {
        mBinding.bar.apply {
            setLeftClickListener {
                onBackPressed()
            }

            setRightClickListener {
                mCategoryViewModel.isDeleteMode.apply {
                    value = !value!!
                }
            }
        }
    }

    override fun initObserver() {
        mCategoryViewModel.apply {
            isDeleteMode.observe(this@CategoryActivity){
                if (it) {
                    mBinding.bar.setRightTitle("完成")
                }else{
                    mBinding.bar.setRightTitle("编辑")
                }
            }
        }
    }

    class CategoryPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return CategoryFragment.newInstance(position)
        }
    }

    /*override fun initListener() {

        //子类
        mSubCategoryAdapter.apply {
            // 单击
            setOnItemClickListener { binding, subCategory ->
                takeUnless { mCategoryViewModel.isDeleteMode.value!! }?.run {
                    lifecycleScope.launch {
                        awaitAll(
                            async {
                                mCategoryViewModel.currentSubCategory.value.takeUnless {
                                    it != null && it.id == subCategory.id
                                }?.let {
                                    // 如果在上一页,跳过不做处理
                                    if (mCategoryViewModel.currentSubCategory.value!!.majorCategoryId != mCategoryViewModel.currentCategory.value!!.id) {
                                        mCategoryViewModel.currentSubCategory.value = subCategory
                                        return@async
                                    }
                                    // 如果在当前页,做处理
                                    val position =
                                        mCategoryViewModel.getCurrentSubCategoryInAdapterPosition(
                                            mCategoryViewModel.currentSubCategory.value!!
                                        )
                                    val viewHolder =
                                        mBinding.rvSubCategory.findViewHolderForLayoutPosition(
                                            position!!
                                        )
                                    viewHolder?.let {
                                        it as SubCategoryAdapter.SubCategoryViewHolder
                                        it.binding.ivItemSubCategorySelected.inVisible()
                                    } ?: mSubCategoryAdapter.notifyItemChanged(position)

                                    mCategoryViewModel.currentSubCategory.value = subCategory
                                }
                            },
                            async {
                                binding.apply {
                                    ivItemSubCategorySelected.visible()
                                }
                            }
                        )
                    }
                }
            }

        }
    }

    override fun initObserver() {
        // 全部主类
        mCategoryViewModel.categories.observe(this) {
            val toJson = Gson().toJson(it)
            Log.d(TAG, "initObserver: $toJson")
            // 如果当前选中为空,默认选中第一个主类,主类为空显示创建提示
            mCategoryViewModel.currentCategory.apply {
                if (value.isNull()) {
                    if (it.isNullOrEmpty()) {
                        // todo 展示提示
                    } else {
                        // 当前没有选中主类时,手动选择第一个
                        if (!mCategoryViewModel.isDeleteMode.value!!) {
                            mMinorCategoryFooterAdapter.showFooterView()
                        }
                        value = it.first()
                    }
                }
            }
            // 更新主类
            mCategoryAdapter.submitList(it)
            // 如果主类已经被删完,自动退出删除模式
            *//*if (it.isEmpty() && mCategoryViewModel.isDeleteMode.value!!) {
                mCategoryViewModel.isDeleteMode.value = false
            }*//*
        }

        // 全部子类
        mCategoryViewModel.subCategories.observe(this) {
            mSubCategoryAdapter.submitList(it)
            // 如果当前没有选择子类/当前子类被删除/子类被删完
            mCategoryViewModel.currentSubCategory.apply {
                if (value.isNull()) {
                    if (!it.isNullOrEmpty()) {
                        value = it.first()
                    }
                }
            }
        }


        // 当前选中子类
        mCategoryViewModel.currentSubCategory.observe(this) {
            mSubCategoryAdapter.setCurrentSubCategory(it)
        }


    }
*/
}