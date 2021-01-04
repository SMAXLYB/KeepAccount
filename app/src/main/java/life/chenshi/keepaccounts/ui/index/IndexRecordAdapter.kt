package life.chenshi.keepaccounts.ui.index

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.database.Category
import life.chenshi.keepaccounts.database.Record
import life.chenshi.keepaccounts.database.RecordType
import life.chenshi.keepaccounts.databinding.ItemBudgetBinding
import life.chenshi.keepaccounts.databinding.ItemBudgetDetailBinding
import life.chenshi.keepaccounts.utils.DateUtils

class IndexRecordAdapter(private var recordListGroupByDay: List<List<Record>>) :
    RecyclerView.Adapter<IndexRecordAdapter.IndexRecordViewHolder>() {


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

        var recordList = recordListGroupByDay[position]
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
                itemBudgetDetailTitle.text = Category.convert2String(it.category)
                // 备注
                if (!it.remark.isNullOrEmpty()) {
                    with(itemBudgetDetailRemark) {
                        text = it.remark
                        visibility = View.VISIBLE
                    }
                }
                // 时间
                itemBudgetDetailCostTime.text = DateUtils.date2String(it.time,DateUtils.HOUR_MINUTE)
                if (it.recordType == RecordType.OUTCOME) {
                    // 圆点
                    with(itemBudgetDetailIcon){
                        setImageResource(R.drawable.item_budget_detail_icon_outcome)
                    }
                    // 金额
                    with(itemBudgetDetailMoney) {
                        text = "-${it.money}"
                        setTextColor(Color.parseColor("#E91E63"))
                    }
                } else {
                    with(itemBudgetDetailIcon){
                        setImageResource(R.drawable.item_budget_detail_icon_income)
                    }
                    with(itemBudgetDetailMoney) {
                        text = "+${it.money}"
                        setTextColor(Color.parseColor("#8bc34a"))
                    }
                }
            }
            itemBudgetDetailContainer.addView(itemBudgetDetailBinding.root)
        }

        holder.binding.apply {
            // 日期
            itemBudgetDate.text = DateUtils.date2MonthDay(recordList[0].time)
            itemBudgetTotalIncome.text = "总收入"
            itemBudgetTotalOutcome.text = "总支出"
        }
    }

    override fun getItemCount(): Int {
        return recordListGroupByDay.size
    }

    fun setData(data: List<List<Record>>) {
        recordListGroupByDay = data
        notifyDataSetChanged()
    }
}