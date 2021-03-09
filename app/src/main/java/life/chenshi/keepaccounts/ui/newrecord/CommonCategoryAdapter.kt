package life.chenshi.keepaccounts.ui.newrecord

import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.common.base.BaseBottomSheetAdapter
import life.chenshi.keepaccounts.database.entity.AbstractCategory
import life.chenshi.keepaccounts.databinding.ItemNewRecordCategoryBinding

class CommonCategoryAdapter(data: List<AbstractCategory>) : BaseBottomSheetAdapter<AbstractCategory, ItemNewRecordCategoryBinding>(data) {
    override fun onBindViewHolder(binding: ItemNewRecordCategoryBinding, itemData: AbstractCategory) {

    }

    override fun getResLayoutId(): Int {
        return R.layout.item_new_record_category
    }
}