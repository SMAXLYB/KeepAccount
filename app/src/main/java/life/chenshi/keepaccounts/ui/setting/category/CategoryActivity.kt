package life.chenshi.keepaccounts.ui.setting.category

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.common.base.BaseActivity
import life.chenshi.keepaccounts.common.utils.ToastUtil
import life.chenshi.keepaccounts.common.utils.inVisible
import life.chenshi.keepaccounts.common.utils.isNull
import life.chenshi.keepaccounts.common.utils.visible
import life.chenshi.keepaccounts.common.view.CustomDialog
import life.chenshi.keepaccounts.database.entity.Category
import life.chenshi.keepaccounts.database.entity.SubCategory
import life.chenshi.keepaccounts.databinding.LayoutAddCategoryBinding
import life.chenshi.keepaccounts.databinding.LayoutAddSubCategoryBinding
import life.chenshi.keepaccounts.databinding.LayoutCategoryBinding
import life.chenshi.keepaccounts.ui.setting.category.adapter.CategoryAdapter
import life.chenshi.keepaccounts.ui.setting.category.adapter.CategoryFooterAdapter
import life.chenshi.keepaccounts.ui.setting.category.adapter.SubCategoryAdapter
import life.chenshi.keepaccounts.ui.setting.category.adapter.SubCategoryFooterAdapter

class CategoryActivity : BaseActivity() {
    companion object {
        private const val TAG = "CategoryActivity"
    }

    private val mCategoryViewModel by viewModels<CategoryViewModel>()
    private val mBinding by bindingContentView<LayoutCategoryBinding>(R.layout.layout_category)

    private val mCategoryAdapter: CategoryAdapter by lazy { CategoryAdapter() }
    private val mCategoryFooterAdapter: CategoryFooterAdapter by lazy { CategoryFooterAdapter() }
    private val mSubCategoryAdapter: SubCategoryAdapter by lazy { SubCategoryAdapter() }
    private val mSubCategoryFooterAdapter: SubCategoryFooterAdapter by lazy { SubCategoryFooterAdapter() }

    override fun initView() {
        // 主类
        mBinding.rvCategory.layoutManager = LinearLayoutManager(this)
        mBinding.rvCategory.adapter = ConcatAdapter(mCategoryAdapter, mCategoryFooterAdapter)

        //子类
        mBinding.rvSubCategory.layoutManager = GridLayoutManager(this, 4)
        mBinding.rvSubCategory.adapter =
            ConcatAdapter(mSubCategoryAdapter, mSubCategoryFooterAdapter)
    }

    override fun initListener() {
        mCategoryAdapter.apply {
            // 主类单击
            setOnItemClickListener { binding, category ->
                // 删除模式不可选择
                takeUnless { mCategoryViewModel.isDeleteMode.value!! }?.run {
                    lifecycleScope.launch {
                        awaitAll(
                            async {
                                // 修改老选中样式: 如果已经选中,则跳过 如果没有选择,跳过
                                mCategoryViewModel.currentCategory.value.takeUnless {
                                    it != null && category.id == it.id
                                }?.let {
                                    // 如果上一次选中在屏幕内,直接修改样式,如果不在,通知更新
                                    val position =
                                        mCategoryViewModel.getCurrentCategoryInAdapterPosition(it)
                                    val viewHolder =
                                        mBinding.rvCategory.findViewHolderForLayoutPosition(position!!)
                                    viewHolder?.let {
                                        viewHolder as CategoryAdapter.CategoryViewHolder
                                        viewHolder.binding.apply {
                                            clItemCategory.isSelected = false
                                            itemCategoryIndicator.inVisible()
                                            tvItemCategory.textSize = 14f
                                            tvItemCategory.isSelected = false
                                        }
                                    } ?: mCategoryAdapter.notifyItemChanged(position)

                                    // 修改选中
                                    mCategoryViewModel.currentCategory.value = category
                                }
                            },
                            // 修改新选中样式, 如果已经选中,则跳过  如果没有选择,不跳过
                            async {
                                binding.apply {
                                    clItemCategory.isSelected = true
                                    itemCategoryIndicator.visible()
                                    tvItemCategory.textSize = 16f
                                    tvItemCategory.isSelected = true
                                }
                            }
                        )
                    }
                } ?: ToastUtil.showShort("请先退出删除模式")
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
                        deleteCategoryWithDialog(category)
                    } else {
                        lifecycleScope.launch {
                            mCategoryViewModel.deleteCategory(category)
                        }
                    }
                }
            }
        }
        // 添加主类
        mCategoryFooterAdapter.setOnItemClickListener {
            takeUnless { mCategoryViewModel.isDeleteMode.value!! }?.run {
                addCategory()
            } ?: ToastUtil.showShort("请先退出删除模式")
        }
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
                                    if (mCategoryViewModel.currentSubCategory.value!!.categoryId != mCategoryViewModel.currentCategory.value!!.id) {
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
            // 长按
            setOnItemLongClickListener { _, _ ->
                mCategoryViewModel.isDeleteMode.value = true
                true
            }
            // 删除
            setOnItemDeleteClickListener { subCategory ->
                mCategoryViewModel.confirmBeforeDelete {
                    if (it) {
                        deleteSubCategoryWithDialog(subCategory)
                    } else {
                        lifecycleScope.launch {
                            mCategoryViewModel.deleteSubCategory(subCategory)
                        }
                    }
                }
            }
        }
        mSubCategoryFooterAdapter.setOnItemClickListener {
            takeUnless { mCategoryViewModel.isDeleteMode.value!! }?.run {
                addSubCategory()
            } ?: ToastUtil.showShort("请先退出删除模式")
        }

        mBinding.tvSubCategory.setOnClickListener {
            mCategoryViewModel.isDeleteMode.value = false
        }
    }

    override fun initObserver() {
        // 全部主类
        mCategoryViewModel.categories.observe(this) {
            // 如果当前选中为空,默认选中第一个主类,主类为空显示创建提示
            mCategoryViewModel.currentCategory.apply {
                if (value.isNull()) {
                    if (it.isNullOrEmpty()) {
                        // todo 展示提示
                    } else {
                        // 当前没有选中主类时,手动选择第一个
                        if (!mCategoryViewModel.isDeleteMode.value!!) {
                            mSubCategoryFooterAdapter.showFooterView()
                        }
                        value = it.first()
                    }
                }
            }
            // 更新主类
            mCategoryAdapter.submitList(it)
            // 如果主类已经被删完,自动退出删除模式
            /*if (it.isEmpty() && mCategoryViewModel.isDeleteMode.value!!) {
                mCategoryViewModel.isDeleteMode.value = false
            }*/
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

        // 当前选中主类
        mCategoryViewModel.currentCategory.observe(this) {
            mCategoryAdapter.setCurrentCategory(it)
            it?.let {
                mCategoryViewModel.getAllSubCategoryByCategoryId(it.id!!)
            }
        }

        // 当前选中子类
        mCategoryViewModel.currentSubCategory.observe(this) {
            mSubCategoryAdapter.setCurrentSubCategory(it)
        }

        // 当前是否为删除模式
        mCategoryViewModel.isDeleteMode.observe(this) {
            mCategoryAdapter.setDeleteMode(it)
            mCategoryFooterAdapter.setFooterViewVisibility(!it)
            mSubCategoryAdapter.setDeleteMode(it)
            // 进入或退出删除时, 展示是否可添加
            // 如果一开始没有选中主类/删除掉了选中主类/删除了所有,则不显示
            if (mCategoryViewModel.currentCategory.value.isNull()) {
                mSubCategoryFooterAdapter.setFooterViewVisibility(false)
            } else {
                mSubCategoryFooterAdapter.setFooterViewVisibility(!it)
            }
        }
    }

    private fun addCategory() {
        CustomDialog.Builder(this@CategoryActivity)
            .setCancelable(false)
            .setTitle("添加主类")
            .setContentView {
                LayoutAddCategoryBinding.inflate(layoutInflater)
            }
            .setPositiveButton("确定") { dialog, binding ->
                binding as LayoutAddCategoryBinding
                val text = binding.etCategoryName.text?.toString()?.trim()
                if (text.isNullOrEmpty()) {
                    binding.etCategoryName.setBackgroundColor(Color.parseColor("#A4FFD1D1"))
                    return@setPositiveButton
                }
                lifecycleScope.launch {
                    kotlin.runCatching {
                        mCategoryViewModel.insertCategory(Category(name = binding.etCategoryName.text.toString()))
                    }.onSuccess {
                        ToastUtil.showSuccess("添加成功")
                        dialog.dismiss()
                    }.onFailure {
                        if (it is SQLiteConstraintException) {
                            ToastUtil.showFail("主类名称重复,换一个试试吧~")
                        } else {
                            throw it
                        }
                    }
                }
            }
            .build()
            .showNow()
    }

    private fun addSubCategory() {
        CustomDialog.Builder(this@CategoryActivity)
            .setCancelable(false)
            .setTitle("添加子类")
            .setContentView {
                LayoutAddSubCategoryBinding.inflate(layoutInflater).apply {
                    tvCategoryName.text = mCategoryViewModel.currentCategory.value!!.name
                }
            }
            .setPositiveButton("确定") { dialog, binding ->
                binding as LayoutAddSubCategoryBinding
                val text = binding.etSubCategoryName.text?.toString()?.trim()
                if (text.isNullOrEmpty()) {
                    binding.etSubCategoryName.setBackgroundColor(Color.parseColor("#A4FFD1D1"))
                    return@setPositiveButton
                }
                lifecycleScope.launch {
                    // 保证在添加子类时,已经选择了主类
                    kotlin.runCatching {
                        mCategoryViewModel.insertSubCategory(
                            SubCategory(
                                name = binding.etSubCategoryName.text.toString(),
                                categoryId = mCategoryViewModel.currentCategory.value!!.id!!
                            )
                        )
                    }.onSuccess {
                        ToastUtil.showSuccess("添加成功")
                        dialog.dismiss()
                    }.onFailure {
                        if (it is SQLiteConstraintException) {
                            // FOREIGN KEY外键约束 唯一约束UNIQUE
                            if (it.message!!.contains("UNIQUE")) {
                                ToastUtil.showFail("子类名称重复,换一个试试吧~")
                            } else if (it.message!!.contains("FOREIGN")) {
                                ToastUtil.showFail("添加失败,主类不存在!")
                                dialog.dismiss()
                            }
                        } else {
                            ToastUtil.showFail("添加失败")
                        }
                    }
                }
            }
            .build()
            .showNow()
    }

    private fun deleteCategoryWithDialog(category: Category) {
        CustomDialog.Builder(this@CategoryActivity)
            .setCancelable(false)
            .setTitle("删除主类")
            .setMessage("\u3000\u3000您正在进行删除操作, 此操作不可逆, 确定继续吗?")
            .setClosedButtonEnable(false)
            .setPositiveButton("确定") { dialog, _ ->
                lifecycleScope.launch {
                    mCategoryViewModel.deleteCategory(category)
                    dialog.dismiss()
                }
            }
            .setNegativeButton("取消")
            .build()
            .showNow()
    }

    private fun deleteSubCategoryWithDialog(subCategory: SubCategory) {
        CustomDialog.Builder(this@CategoryActivity)
            .setCancelable(false)
            .setTitle("删除子类")
            .setMessage("\u3000\u3000您正在进行删除操作, 此操作不可逆, 确定继续吗?")
            .setClosedButtonEnable(false)
            .setPositiveButton("确定") { dialog, _ ->
                lifecycleScope.launch {
                    mCategoryViewModel.deleteSubCategory(subCategory)
                    dialog.dismiss()
                }
            }
            .setNegativeButton("取消")
            .build()
            .showNow()
    }
}