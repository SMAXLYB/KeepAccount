package life.chenshi.keepaccounts.ui.anaylze

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.bean.SumMoneyGroupByCategory

class AnalyzeProportionAdapter(val data: List<SumMoneyGroupByCategory>) : BaseAdapter() {
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val str = data[position]
        val view: View
        val viewHolder: ProportionViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_proportion_detail, parent, false)
            viewHolder = ProportionViewHolder()
            viewHolder.type = view.findViewById(R.id.tv_item_proportion_detail_category) as TextView
            viewHolder.description =
                view.findViewById(R.id.tv_item_proportion_detail_description) as TextView
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ProportionViewHolder
        }
        // viewHolder.type?.text = str
        // viewHolder.description?.text = "总共消费 ${} 元, 花费了 ${} 笔"
        return view
    }

    class ProportionViewHolder() {
        var type: TextView? = null
        var description: TextView? = null
    }
}