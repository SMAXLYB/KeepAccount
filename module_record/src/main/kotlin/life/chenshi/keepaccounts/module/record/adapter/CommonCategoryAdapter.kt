package life.chenshi.keepaccounts.module.record.adapter

import life.chenshi.keepaccounts.module.common.base.BaseAdapter
import life.chenshi.keepaccounts.module.common.database.entity.AbstractCategory
import life.chenshi.keepaccounts.module.common.utils.getColorFromAttr
import life.chenshi.keepaccounts.module.record.R
import life.chenshi.keepaccounts.module.record.databinding.RecordItemNewRecordCategoryBinding

class CommonCategoryAdapter(data: List<AbstractCategory>) : BaseAdapter<AbstractCategory, RecordItemNewRecordCategoryBinding>(data) {
    private var mCurrentCategory: AbstractCategory? = null

    override fun onBindViewHolder(binding: RecordItemNewRecordCategoryBinding, itemData: AbstractCategory) {
        binding.tvCategory.text = itemData.name
        // 如果不是最后一个,可以设置文字
        if (itemData.id != -1) {
            binding.tivCategory.setText(itemData.name)
        }
        binding.tivCategory.setBackgroundColor(binding.root.context.getColorFromAttr(R.attr.colorPrimaryVariant))
        // 如果被选中
        mCurrentCategory?.let {
            if (itemData == it) {
                binding.tivCategory.setBackgroundColor(binding.root.context.getColorFromAttr(R.attr.colorPrimary))
            }
        }
    }

    fun setCurrentCategory(abstractCategory: AbstractCategory) {
        mCurrentCategory = abstractCategory
        notifyDataSetChanged()
    }

    override fun getResLayoutId(): Int {
        return R.layout.record_item_new_record_category
    }
}