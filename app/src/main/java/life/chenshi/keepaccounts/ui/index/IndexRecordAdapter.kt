package life.chenshi.keepaccounts.ui.index

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.common.utils.DateUtil
import life.chenshi.keepaccounts.constant.RECORD_TYPE_OUTCOME
import life.chenshi.keepaccounts.database.entity.Record
import life.chenshi.keepaccounts.databinding.ItemBudgetBinding
import life.chenshi.keepaccounts.databinding.ItemBudgetDetailBinding

class IndexRecordAdapter(private var recordListGroupByDay: List<List<Record>>) :
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
                // itemBudgetDetailTitle.text = Category2.convert2String(it.category)
                // 备注
                if (!it.remark.isNullOrEmpty()) {
                    with(itemBudgetDetailRemark) {
                        text = it.remark
                        visibility = View.VISIBLE
                    }
                }
                // 时间
                itemBudgetDetailCostTime.text = DateUtil.date2String(
                    it.time,
                    DateUtil.HOUR_MINUTE
                )
                if (it.recordType == RECORD_TYPE_OUTCOME) {
                    // 圆点
                    with(itemBudgetDetailIcon) {
                        setImageResource(R.drawable.item_budget_detail_icon_outcome)
                    }
                    // 金额
                    with(itemBudgetDetailMoney) {
                        text = "-${it.money}"
                        setTextColor(Color.parseColor("#E91E63"))
                    }
                } else {
                    with(itemBudgetDetailIcon) {
                        setImageResource(R.drawable.item_budget_detail_icon_income)
                    }
                    with(itemBudgetDetailMoney) {
                        text = "+${it.money}"
                        setTextColor(Color.parseColor("#8bc34a"))
                    }
                }
                mLongClickListener?.let { _ ->
                    root.setOnClickListener { _ ->
                        mClickListener?.invoke(it)
                    }
                    root.setOnLongClickListener { _ ->
                        mLongClickListener?.invoke(it)
                        true
                    }
                }
            }
            itemBudgetDetailContainer.addView(itemBudgetDetailBinding.root)
        }

        holder.binding.apply {
            // 日期
            itemBudgetDate.text = DateUtil.date2MonthDay(recordList[0].time)
            itemBudgetTotalIncome.text = ""
            itemBudgetTotalOutcome.text = "金额"
        }
    }

    override fun getItemCount(): Int {
        return recordListGroupByDay.size
    }

    fun setData(data: List<List<Record>>) {
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