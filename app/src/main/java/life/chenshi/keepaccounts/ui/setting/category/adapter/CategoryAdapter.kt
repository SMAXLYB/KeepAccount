package life.chenshi.keepaccounts.ui.setting.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.common.utils.setVisibility
import life.chenshi.keepaccounts.database.entity.Category
import life.chenshi.keepaccounts.databinding.ItemCategoryAddBinding
import life.chenshi.keepaccounts.databinding.ItemCategoryBinding

typealias ItemDeleteListener<T> = (T) -> Unit
typealias ItemClickListener<VB, T> = (VB, T) -> Unit
typealias ItemLongClickListener<T> = (ViewDataBinding, T) -> Boolean

class CategoryAdapter : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var onItemClick: ItemClickListener<ItemCategoryBinding, Category>? = null
    private var onItemLongClick: ItemLongClickListener<Category>? = null
    private var onItemDeleteClick: ItemDeleteListener<Category>? = null
    private var mIsDeleteMode = false
    private var mCurrentCategory: Category? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = DataBindingUtil.inflate<ItemCategoryBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_category,
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    fun setCurrentCategory(category: Category?) {
        this.mCurrentCategory = category
    }

    fun setDeleteMode(turnOn: Boolean = true) {
        takeIf {
            mIsDeleteMode != turnOn
        }?.run {
            this.mIsDeleteMode = turnOn
            notifyDataSetChanged()
        }
    }

    fun setOnItemClickListener(listener: ItemClickListener<ItemCategoryBinding,Category>) {
        this.onItemClick = listener
    }

    fun setOnItemLongClickListener(listener: ItemLongClickListener<Category>) {
        this.onItemLongClick = listener
    }

    fun setOnItemDeleteClickListener(listener: ItemDeleteListener<Category>) {
        this.onItemDeleteClick = listener
    }

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val category = getItem(position)
            binding.apply {
                // 点击和长按使事件
                binding.root.setOnClickListener {
                    onItemClick?.invoke(this, category)
                }
                binding.root.setOnLongClickListener {
                    onItemLongClick?.invoke(this, category) ?: false
                }

                // 样式修改
                clItemCategory.isSelected =
                    category.id == mCurrentCategory?.id && !mIsDeleteMode
                itemCategoryIndicator.setVisibility(category.id == mCurrentCategory?.id && !mIsDeleteMode)
                tvItemCategory.isSelected =
                    category.id == mCurrentCategory?.id && !mIsDeleteMode
                tvItemCategory.textSize =
                    if (category.id == mCurrentCategory?.id && !mIsDeleteMode) {
                        16f
                    } else {
                        14f
                    }

                ivItemCategoryDelete.setVisibility(mIsDeleteMode)
                tvItemCategory.text = category.name

                // 删除事件
                ivItemCategoryDelete.setOnClickListener {
                    onItemDeleteClick?.invoke(category)
                }
            }
        }
    }
}

class CategoryFooterAdapter :
    RecyclerView.Adapter<CategoryFooterAdapter.CategoryFooterViewHolder>() {
    private var mItemCount = 1
    private var mOnItemClickListener: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryFooterViewHolder {
        val binding =
            ItemCategoryAddBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryFooterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryFooterViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = mItemCount

    inner class CategoryFooterViewHolder(private val binding: ItemCategoryAddBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.setOnClickListener {
                mOnItemClickListener?.invoke()
            }
        }
    }

    fun setOnItemClickListener(listener: () -> Unit) {
        this.mOnItemClickListener = listener
    }

    fun setFooterViewVisibility(show: Boolean) {
        if (show) {
            showFooterView()
        } else {
            hideFooterView()
        }
    }

    fun hideFooterView() {
        mItemCount = 0
        notifyDataSetChanged()
    }

    fun showFooterView() {
        mItemCount = 1
        notifyDataSetChanged()
    }
}