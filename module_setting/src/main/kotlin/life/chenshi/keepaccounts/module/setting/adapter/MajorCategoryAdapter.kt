package life.chenshi.keepaccounts.module.setting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import life.chenshi.keepaccounts.module.common.database.entity.MajorCategory
import life.chenshi.keepaccounts.module.common.utils.setVisibility
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.databinding.SettingItemCategoryAddBinding
import life.chenshi.keepaccounts.module.setting.databinding.SettingItemCategoryBinding

typealias ItemDeleteListener<T> = (T) -> Unit
typealias ItemClickListener<VB, T> = (VB, T) -> Unit
typealias ItemLongClickListener<T> = (ViewDataBinding, T) -> Boolean

class MajorCategoryAdapter : ListAdapter<MajorCategory, MajorCategoryAdapter.CategoryViewHolder>(
    DIFF_CALLBACK
) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MajorCategory>() {
            override fun areItemsTheSame(oldItem: MajorCategory, newItem: MajorCategory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MajorCategory, newItem: MajorCategory): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var onItemClick: ItemClickListener<SettingItemCategoryBinding, MajorCategory>? = null
    private var onItemLongClick: ItemLongClickListener<MajorCategory>? = null
    private var onItemDeleteClick: ItemDeleteListener<MajorCategory>? = null
    private var mIsDeleteMode = false
    private var mCurrentMajorCategory: MajorCategory? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = DataBindingUtil.inflate<SettingItemCategoryBinding>(
            LayoutInflater.from(parent.context),
            R.layout.setting_item_category,
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

    fun setCurrentCategory(majorCategory: MajorCategory?) {
        this.mCurrentMajorCategory = majorCategory
    }

    fun setDeleteMode(turnOn: Boolean = true) {
        takeIf {
            mIsDeleteMode != turnOn
        }?.run {
            this.mIsDeleteMode = turnOn
            notifyDataSetChanged()
        }
    }

    fun setOnItemClickListener(listener: ItemClickListener<SettingItemCategoryBinding, MajorCategory>) {
        this.onItemClick = listener
    }

    fun setOnItemLongClickListener(listener: ItemLongClickListener<MajorCategory>) {
        this.onItemLongClick = listener
    }

    fun setOnItemDeleteClickListener(listener: ItemDeleteListener<MajorCategory>) {
        this.onItemDeleteClick = listener
    }

    inner class CategoryViewHolder(val binding: SettingItemCategoryBinding) :
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
                    category.id == mCurrentMajorCategory?.id
                itemCategoryIndicator.setVisibility(category.id == mCurrentMajorCategory?.id)
                tvItemCategory.isSelected =
                    category.id == mCurrentMajorCategory?.id
                tvItemCategory.textSize =
                    if (category.id == mCurrentMajorCategory?.id) {
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

class MajorCategoryFooterAdapter :
    RecyclerView.Adapter<MajorCategoryFooterAdapter.CategoryFooterViewHolder>() {
    private var mItemCount = 1
    private var mOnItemClickListener: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryFooterViewHolder {
        val binding =
            SettingItemCategoryAddBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryFooterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryFooterViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = mItemCount

    inner class CategoryFooterViewHolder(private val binding: SettingItemCategoryAddBinding) :
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