package life.chenshi.keepaccounts.ui.newrecord

import android.graphics.Color
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.common.base.BaseAdapter
import life.chenshi.keepaccounts.database.entity.AbstractCategory
import life.chenshi.keepaccounts.databinding.ItemNewRecordCategoryBinding

class CommonCategoryAdapter(data: List<AbstractCategory>) : BaseAdapter<AbstractCategory, ItemNewRecordCategoryBinding>(data) {
    private var mCurrentCategory: AbstractCategory? = null

    override fun onBindViewHolder(binding: ItemNewRecordCategoryBinding, itemData: AbstractCategory) {
        binding.tvCategory.text = itemData.name
        // 如果不是最后一个,可以设置文字
        if (itemData.id != -1) {
            binding.tivCategory.setText(itemData.name)
        }
        binding.tivCategory.setBackgroundColor(Color.parseColor("#4D03A9F4"))
        // 如果被选中
        mCurrentCategory?.let {
            if (itemData == it) {
                binding.tivCategory.setBackgroundColor(Color.parseColor("#03A9F4"))
            }
        }
    }

    fun setCurrentCategory(abstractCategory: AbstractCategory) {
        mCurrentCategory = abstractCategory
        notifyDataSetChanged()
    }

    override fun getResLayoutId(): Int {
        return R.layout.item_new_record_category
    }
}