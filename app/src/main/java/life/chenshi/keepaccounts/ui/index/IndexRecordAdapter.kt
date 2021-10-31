package life.chenshi.keepaccounts.ui.index

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.database.bean.RecordWithCategoryBean
import life.chenshi.keepaccounts.database.entity.Record
import life.chenshi.keepaccounts.databinding.ItemBudgetBinding
import life.chenshi.keepaccounts.databinding.ItemBudgetDetailBinding
import life.chenshi.keepaccounts.module.common.constant.RECORD_TYPE_OUTCOME
import life.chenshi.keepaccounts.module.common.utils.DateUtil

class IndexRecordAdapter(private var recordListGroupByDay: List<List<RecordWithCategoryBean>>) :
    RecyclerView.Adapter<IndexRecordAdapter.IndexRecordViewHolder>() {
    private var mLongClickListener: ((Record) -> Unit)? = null
    private var mClickListener: ((Record) -> Unit)? = null

    class IndexRecordViewHolder(var binding: ItemBudgetBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndexRecordViewHolder {
        val itemBudgetBinding = DataBindingUtil.inflate<ItemBudgetBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_budget,
            parent,
            false
        )
        return IndexRecordViewHolder(itemBudgetBinding)
    }

    override fun onBindViewHolder(holder: IndexRecordViewHolder, position: Int) {

        val recordList = recordListGroupByDay[position]
        val itemBudgetDetailContainer = holder.binding.itemBudgetDetailContainer
        // viewHolder会被重复使用，所以要删除之前的view
        itemBudgetDetailContainer.removeAllViews()
        recordList.forEach {
            val itemBudgetDetailBinding = ItemBudgetDetailBinding.inflate(
                LayoutInflater.from(itemBudgetDetailContainer.context),
                itemBudgetDetailContainer,
                false
            )
            itemBudgetDetailBinding.apply {
                // 支出主题
                itemBudgetDetailIcon.setText(it.minorCategory?.name ?: it.majorCategory.name)
                itemBudgetDetailTitle.text = it.majorCategory.name
                // 时间
                itemBudgetDetailCostTime.text = DateUtil.date2String(
                    it.record.time,
                    DateUtil.HOUR_MINUTE
                )
                if (it.record.recordType == RECORD_TYPE_OUTCOME) {
                    // 金额
                    with(itemBudgetDetailMoney) {
                        text = "-${it.record.money}"
                        setTextColor(Color.parseColor("#E91E63"))
                    }
                } else {
                    with(itemBudgetDetailMoney) {
                        text = "+${it.record.money}"
                        setTextColor(Color.parseColor("#8bc34a"))
                    }
                }
                mLongClickListener?.let { _ ->
                    root.setOnClickListener { _ ->
                        mClickListener?.invoke(it.record)
                    }
                    root.setOnLongClickListener { _ ->
                        mLongClickListener?.invoke(it.record)
                        true
                    }
                }
            }
            itemBudgetDetailContainer.addView(itemBudgetDetailBinding.root)
        }

        holder.binding.apply {
            // 日期
            itemBudgetDate.text = DateUtil.date2MonthDay(recordList[0].record.time)
            // itemBudgetTotalIncome.text = ""
            // itemBudgetTotalOutcome.text = "金额"
        }
    }

    override fun getItemCount(): Int {
        return recordListGroupByDay.size
    }

    fun setData(data: List<List<RecordWithCategoryBean>>) {
        recordListGroupByDay = data
        notifyDataSetChanged()
    }

    fun setOnItemLongClickListener(listener: (Record) -> Unit) {
        mLongClickListener = listener
    }

    fun setOnClickListener(listener: (Record) -> Unit) {
        mClickListener = listener
    }
}