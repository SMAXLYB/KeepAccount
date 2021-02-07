package life.chenshi.keepaccounts.ui.setting.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.common.adapter.HeaderFooterAdapter
import life.chenshi.keepaccounts.databinding.ItemSubCategoryAddBinding
import life.chenshi.keepaccounts.databinding.ItemSubCategoryBinding

class SubCategoryAdapter() : HeaderFooterAdapter() {

    override fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemSubCategoryBinding>(LayoutInflater.from(parent.context), R.layout.item_sub_category, parent, false)
        return NormalViewHolder(binding)
    }

    override fun onCreateFooterViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemSubCategoryAddBinding>(LayoutInflater.from(parent.context), R.layout.item_sub_category_add, parent, false)
        return FooterViewHolder(binding)
    }

    override fun onBindFooterViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    @Suppress("CAST_NEVER_SUCCEEDS")
    override fun onBindNormalViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val subCategory = mNormals[position].data as String
        holder as NormalViewHolder
        holder.binding.apply {
            this.tvSubCategoryName.text = subCategory
        }
    }

    class FooterViewHolder(val binding: ItemSubCategoryAddBinding) : RecyclerView.ViewHolder(binding.root)

    class NormalViewHolder(val binding: ItemSubCategoryBinding) : RecyclerView.ViewHolder(binding.root)
}