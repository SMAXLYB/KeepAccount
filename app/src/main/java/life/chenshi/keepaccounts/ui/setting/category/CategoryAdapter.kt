package life.chenshi.keepaccounts.ui.setting.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.common.adapter.HeaderFooterAdapter
import life.chenshi.keepaccounts.databinding.ItemCategoryAddBinding
import life.chenshi.keepaccounts.databinding.ItemCategoryBinding

class CategoryAdapter() : HeaderFooterAdapter() {

    override fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemCategoryBinding>(LayoutInflater.from(parent.context), R.layout.item_category, parent, false)
        return NormalViewHolder(binding)
    }

    override fun onCreateFooterViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemCategoryAddBinding>(LayoutInflater.from(parent.context), R.layout.item_category_add, parent, false)
        return FooterViewHolder(binding)
    }

    override fun onBindFooterViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}

    @Suppress("CAST_NEVER_SUCCEEDS")
    override fun onBindNormalViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val category = mNormals[position].data as String
        holder as NormalViewHolder
        holder.binding.apply {
            this.ivItemCategoryDelete
            this.itemCategoryIndicator
            this.tvItemCategory.text = category
        }
    }

    class FooterViewHolder(val binding: ItemCategoryAddBinding) : RecyclerView.ViewHolder(binding.root)

    class NormalViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)
}