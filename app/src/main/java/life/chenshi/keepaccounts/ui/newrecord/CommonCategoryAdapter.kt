package life.chenshi.keepaccounts.ui.newrecord

import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.common.base.BaseAdapter
import life.chenshi.keepaccounts.database.entity.MinorCategory
import life.chenshi.keepaccounts.databinding.ItemNewRecordCategoryBinding

class CommonCategoryAdapter(data: List<MinorCategory>) : BaseAdapter<MinorCategory, ItemNewRecordCategoryBinding>(data) {
    
    override fun onBindViewHolder(binding: ItemNewRecordCategoryBinding, itemData: MinorCategory) {
        binding.tvCategory.text = itemData.name
        // binding.tivCategory.setText(itemData.name)
    }

    override fun getResLayoutId(): Int {
        return R.layout.item_new_record_category
    }
}