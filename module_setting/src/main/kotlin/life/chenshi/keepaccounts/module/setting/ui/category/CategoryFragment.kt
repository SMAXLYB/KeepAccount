package life.chenshi.keepaccounts.module.setting.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.module.common.base.BaseFragment
import life.chenshi.keepaccounts.module.common.constant.RECORD_TYPE_OUTCOME
import life.chenshi.keepaccounts.module.common.utils.ToastUtil
import life.chenshi.keepaccounts.module.common.utils.inVisible
import life.chenshi.keepaccounts.module.common.utils.isNull
import life.chenshi.keepaccounts.module.common.utils.visible
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.adapter.MajorCategoryAdapter
import life.chenshi.keepaccounts.module.setting.adapter.MajorCategoryFooterAdapter
import life.chenshi.keepaccounts.module.setting.adapter.MinorCategoryAdapter
import life.chenshi.keepaccounts.module.setting.adapter.MinorCategoryFooterAdapter
import life.chenshi.keepaccounts.module.setting.databinding.SettingFragmentCategoryBinding
import life.chenshi.keepaccounts.module.setting.vm.CategoryViewModel

/**
 * @description: 分类fragment, 收入支出共用一个fragment
 * @author smaxlyb
 * @date 2021年03月27日 13:05
 */
class CategoryFragment : BaseFragment() {

    private lateinit var mBinding: SettingFragmentCategoryBinding
    private var recordType = RECORD_TYPE_OUTCOME
    private val mCategoryViewModel by activityViewModels<CategoryViewModel>()
    private val mMajorCategoryAdapter: MajorCategoryAdapter by lazy { MajorCategoryAdapter() }
    private val mMajorCategoryFooterAdapter: MajorCategoryFooterAdapter by lazy { MajorCategoryFooterAdapter() }
    private val mMinorCategoryAdapter: MinorCategoryAdapter by lazy { MinorCategoryAdapter() }
    private val mMinorCategoryFooterAdapter: MinorCategoryFooterAdapter by lazy { MinorCategoryFooterAdapter() }

    companion object {
        private const val TAG = "CategoryFragment"
        private const val RECORD_TYPE = "record_type"

        fun newInstance(categoryType: Int): CategoryFragment {
            return CategoryFragment().apply {
                arguments = bundleOf(RECORD_TYPE to categoryType)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.setting_fragment_category, container, false)
        return mBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        initListener()
        initObserver()
    }

    private fun initData() {
        arguments?.getInt(RECORD_TYPE, RECORD_TYPE_OUTCOME)?.run {
            recordType = this
        }
    }

    private fun initView() {
        // 主类
        mBinding.rvMajorCategory.layoutManager = LinearLayoutManager(activity)
        mBinding.rvMajorCategory.adapter = ConcatAdapter(mMajorCategoryAdapter, mMajorCategoryFooterAdapter)

        //子类
        mBinding.rvMinorCategory.layoutManager = GridLayoutManager(activity, 4)
        mBinding.rvMinorCategory.adapter =
            ConcatAdapter(mMinorCategoryAdapter, mMinorCategoryFooterAdapter)
    }

    private fun initListener() {

        mMajorCategoryAdapter.apply {
            // 主类点击
            setOnItemClickListener { binding, category ->
                // 如果已经被选中,直接返回,不做处理
                mCategoryViewModel.currentMajorCategory.value?.run {
                    if (this == category){
                        return@setOnItemClickListener
                    }
                }

                // 修改当前选中样式
                binding.apply {
                    clItemCategory.isSelected = true
                    itemCategoryIndicator.visible()
                    tvItemCategory.textSize = 16f
                    tvItemCategory.isSelected = true
                }

                // 修改选中
                mCategoryViewModel.currentMajorCategory.apply {
                    mCategoryViewModel.lastMajorCategory.value = this.value
                    this.value = category
                }
            }

            // 主类长按
            setOnItemLongClickListener { _, _ ->
                mCategoryViewModel.isDeleteMode.value = true
                true
            }

            // 删除主类
            setOnItemDeleteClickListener { category ->
                mCategoryViewModel.confirmBeforeDelete {
                    if (it) {
                        mCategoryViewModel.deleteMajorCategoryWithDialog(requireActivity(), category)
                    } else {
                        mCategoryViewModel.deleteMajorCategory(category)
                    }
                }
            }
        }

        mMinorCategoryAdapter.apply {
            // 单击
            setOnItemClickListener { binding, minorCategory ->
                // 当前是新建页面来的,并且不是处于删除模式,点击事件才有效
                takeIf { !mCategoryViewModel.isDeleteMode.value!! && !mCategoryViewModel.business.value.isNull() }?.run {
                    // 如果已经选中,跳过
                    if(mCategoryViewModel.currentMinorCategory.value == minorCategory){
                        return@setOnItemClickListener
                    }

                    binding.apply {
                        ivItemSubCategorySelected.visible()
                    }
                    mCategoryViewModel.currentMinorCategory.apply {
                        mCategoryViewModel.lastMinorCategory.value = value
                        value = minorCategory
                    }
                }
            }

            // 长按
            setOnItemLongClickListener { _, _ ->
                mCategoryViewModel.isDeleteMode.value = true
                true
            }
            // 删除
            setOnItemDeleteClickListener { subCategory ->
                mCategoryViewModel.confirmBeforeDelete {
                    if (it) {
                        mCategoryViewModel.deleteMinorCategoryWithDialog(requireActivity(), subCategory)
                    } else {
                        mCategoryViewModel.deleteMinorCategory(subCategory)
                    }
                }
            }
        }

        // 添加主类
        mMajorCategoryFooterAdapter.setOnItemClickListener {
            takeUnless { mCategoryViewModel.isDeleteMode.value!! }?.run {
                mCategoryViewModel.addCategory(requireActivity(), recordType)
            } ?: ToastUtil.showShort("请先退出删除模式")
        }

        // 添加子类
        mMinorCategoryFooterAdapter.setOnItemClickListener {
            takeUnless { mCategoryViewModel.isDeleteMode.value!! }?.run {
                mCategoryViewModel.addSubCategory(requireActivity(), recordType)
            } ?: ToastUtil.showShort("请先退出删除模式")
        }
    }

    private fun initObserver() {

        mCategoryViewModel.apply {
            // 主类监听
            majorCategories.map { list ->
                list.filter { it.recordType == recordType }
            }.observe(viewLifecycleOwner) {
                // 更新主类
                mMajorCategoryAdapter.submitList(it)
                // 如果当前选中为空,默认选中第一个主类,主类为空显示创建提示
                currentMajorCategory.apply {
                    // 如果已经有选中,不做任何操作;第二个fragment读取时已经不为空
                    if (this.value.isNull()) {
                        if (it.isNullOrEmpty()) {
                            // todo 展示提示
                        } else {
                            // 当前没有选中主类时,手动选择第一个
                            this.value = it.first()
                        }
                    }
                }
            }
            // 上一次选中主类监听
            lastMajorCategory.observe(viewLifecycleOwner) {
                // 旧的变更
                // 如果上一次选中不为空,在屏幕内,直接修改样式,如果不在,通知更新
                lifecycleScope.launch {
                    it?.let {
                        val position = getCurrentMajorCategoryInAdapterPosition(it, recordType)
                        position?.let {
                            val viewHolder =
                                mBinding.rvMajorCategory.findViewHolderForLayoutPosition(position)
                            viewHolder?.let { holder ->
                                holder as MajorCategoryAdapter.CategoryViewHolder
                                holder.binding.apply {
                                    clItemCategory.isSelected = false
                                    itemCategoryIndicator.inVisible()
                                    tvItemCategory.textSize = 14f
                                    tvItemCategory.isSelected = false
                                }
                            } ?: mMajorCategoryAdapter.notifyItemChanged(position)

                        }
                    }
                    // 置空子类
                    currentMinorCategory.value = null
                }
            }
            // 当前选中主类监听
            currentMajorCategory.observe(viewLifecycleOwner) {
                // 当前选中被删除则直接跳过
                if (it.isNull()) {
                    return@observe
                }
                // 默认隐藏footer
                mMinorCategoryFooterAdapter.hideFooterView()
                // 如果当前选择类型还是一致,并且不是删除模式,就显示footer
                if (it!!.recordType == recordType && !mCategoryViewModel.isDeleteMode.value!!) {
                    mMinorCategoryFooterAdapter.showFooterView()
                }
                // 新的变更
                mMajorCategoryAdapter.setCurrentCategory(it)
                getAllMinorCategoryByMajorCategoryId(it.id!!)
            }

            // 子类监听
            minorCategories.map { list ->
                list.filter { it.recordType == recordType }
            }.observe(viewLifecycleOwner) {
                mMinorCategoryAdapter.submitList(it)
                // 从其他页面中进来才有选择
                business.value?.run {
                    currentMinorCategory.apply {
                        if (value.isNull()) {
                            if (!it.isNullOrEmpty()) {
                                value = it.first()
                            }
                        }
                    }
                }
            }

            // 上一次选中子类监听
            lastMinorCategory.observe(viewLifecycleOwner) {
                // 如果为空不做处理
                lifecycleScope.launch {
                    it?.let {
                        // 如果在当前页,做处理
                        val position = getCurrentMinorCategoryInAdapterPosition(it)
                        position?.let {
                            val viewHolder =
                                mBinding.rvMinorCategory.findViewHolderForLayoutPosition(position)
                            viewHolder?.let { holder ->
                                holder as MinorCategoryAdapter.MinorCategoryViewHolder
                                holder.binding.ivItemSubCategorySelected.inVisible()
                            } ?: mMinorCategoryAdapter.notifyItemChanged(position)
                        }
                    }
                }
            }

            // 当前选中子类监听
            currentMinorCategory.observe(viewLifecycleOwner) {
                if (it.isNull()) {
                    return@observe
                }

                mMinorCategoryAdapter.setCurrentMinorCategory(it)
            }

            // 当前是否为删除模式
            isDeleteMode.observe(viewLifecycleOwner) {
                mMajorCategoryAdapter.setDeleteMode(it)
                mMajorCategoryFooterAdapter.setFooterViewVisibility(!it)
                mMinorCategoryAdapter.setDeleteMode(it)
                // 进入或退出删除时, 展示是否可添加
                // 如果一开始没有选中主类/删除掉了选中主类/删除了所有,则不显示
                if (currentMajorCategory.value.isNull()) {
                    mMinorCategoryFooterAdapter.setFooterViewVisibility(false)
                } else {
                    mMinorCategoryFooterAdapter.setFooterViewVisibility(!it && currentMajorCategory.value!!.recordType == recordType)
                }
            }
        }
    }
}