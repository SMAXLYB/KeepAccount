package life.chenshi.keepaccounts.ui.anaylze

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.bean.SumMoneyGroupByCategoryBean

class AnalyzeProportionAdapter(private var records: List<SumMoneyGroupByCategoryBean>) : BaseAdapter() {

    private val colors =
        arrayOf<String>("#FFCC00", "#99CCFF", "#ffc6ff", "#FF9999", "#CCCCFF", "#aacc00")

    override fun getCount(): Int {
        return records.size
    }

    override fun getItem(position: Int): Any {
        return records[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    fun setData(data: List<SumMoneyGroupByCategoryBean>) {
        records = data
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val bean = records[position]
        val view: View
        val viewHolder: ProportionViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_proportion_detail, parent, false)
            viewHolder = ProportionViewHolder()
            viewHolder.icon = view.findViewById(R.id.iv_item_proportion_detail_icon) as ImageView
            viewHolder.category =
                view.findViewById(R.id.tv_item_proportion_detail_category) as TextView
            viewHolder.description =
                view.findViewById(R.id.tv_item_proportion_detail_description) as TextView
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ProportionViewHolder
        }
        viewHolder.icon?.setBackgroundColor(Color.parseColor(colors[position]))
        viewHolder.category?.text = bean.getCategory()
        viewHolder.description?.text = "总共 ${bean.getMoney()} 元, 包含 ${bean.getCount()} 笔交易"
        return view
    }

    private class ProportionViewHolder() {
        var icon: ImageView? = null
        var category: TextView? = null
        var description: TextView? = null
    }
}