package life.chenshi.keepaccounts.ui.anaylze

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import life.chenshi.keepaccounts.R

class MyMarkerView(context: Context, resId: Int) : MarkerView(context, resId) {

    private var tvContent: TextView = findViewById<TextView>(R.id.tv_marker_content)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.let {
            tvContent.text = "${it.y}元"
        }
        super.refreshContent(e, highlight)
    }

    private var mOffset: MPPointF? = null

    override fun getOffset(): MPPointF {
        if (mOffset == null) {
            mOffset = MPPointF(-(width / 2).toFloat(), (-height * 1.5).toFloat())
        }

        return mOffset as MPPointF
    }
}