package life.chenshi.keepaccounts.ui.setting.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.common.utils.setVisibility
import life.chenshi.keepaccounts.database.entity.SubCategory
import life.chenshi.keepaccounts.databinding.ItemSubCategoryAddBinding
import life.chenshi.keepaccounts.databinding.ItemSubCategoryBinding

class SubCategoryAdapter :
    ListAdapter<SubCategory, SubCategoryAdapter.SubCategoryViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SubCategory>() {
            override fun areItemsTheSame(oldItem: SubCategory, newItem: SubCategory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SubCategory, newItem: SubCategory): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var onItemClick: ItemClickListener<ItemSubCategoryBinding,SubCategory>? = null
    private var onItemLongClick: ItemLongClickListener<SubCategory>? = null
    private var onItemDeleteClick: ItemDeleteListener<SubCategory>? = null
    private var mIsDeleteMode = false
    private var mCurrentSubCategory: SubCategory? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        val binding = DataBindingUtil.inflate<ItemSubCategoryBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_sub_category,
            parent,
            false
        )
        return SubCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        holder.bind(position)
    }

    fun setCurrentSubCategory(category: SubCategory?) {
        this.mCurrentSubCategory = category
    }

    fun setDeleteMode(turnOn: Boolean = true) {
        takeIf {
            mIsDeleteMode != turnOn
        }?.run {
            this.mIsDeleteMode = turnOn
            notifyDataSetChanged()
        }
    }

    fun setOnItemClickListener(listener: ItemClickListener<ItemSubCategoryBinding,SubCategory>) {
        this.onItemClick = listener
    }

    fun setOnItemLongClickListener(listener: ItemLongClickListener<SubCategory>) {
        this.onItemLongClick = listener
    }

    fun setOnItemDeleteClickListener(listener: ItemDeleteListener<SubCategory>) {
        this.onItemDeleteClick = listener
    }

    inner class SubCategoryViewHolder(val binding: ItemSubCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val subCategory = getItem(position)
            binding.apply {
                // 点击和长按使事件
                binding.root.setOnClickListener {
                    onItemClick?.invoke(this, subCategory)
                }
                binding.root.setOnLongClickListener {
                    onItemLongClick?.invoke(this, subCategory) ?: false
                }

                // 样式修改
                ivItemSubCategorySelected.setVisibility(subCategory.id == mCurrentSubCategory?.id && !mIsDeleteMode)
                ivItemSubCategoryDelete.setVisibility(mIsDeleteMode)
                tvItemSubCategoryName.text = subCategory.name

                // 删除事件
                ivItemSubCategoryDelete.setOnClickListener {
                    onItemDeleteClick?.invoke(subCategory)
                }
            }
        }
    }
}

class SubCategoryFooterAdapter :
    RecyclerView.Adapter<SubCategoryFooterAdapter.SubCategoryFooterViewHolder>() {
    private var mItemCount = 0
    private var mOnItemClickListener: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryFooterViewHolder {
        val binding =
            ItemSubCategoryAddBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubCategoryFooterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubCategoryFooterViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = mItemCount

    inner class SubCategoryFooterViewHolder(private val binding: ItemSubCategoryAddBinding) :
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