package life.chenshi.keepaccounts.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class HeaderFooterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        /* 基本类型 */
        private const val ITEM_TYPE_FOOTER = 200000
        private const val ITEM_TYPE_HEADER = 100000
        private const val ITEM_TYPE_NORMAL = 0
    }

    private var needFullSpan = true

    /*********** 保存了每个view节点的全部信息 *********/
    protected val mHeaders = arrayListOf<DataBean>()
    protected val mFooters = arrayListOf<DataBean>()
    protected val mNormals = arrayListOf<DataBean>()

    override fun getItemViewType(position: Int): Int {
        if (isHeaderViewPosition(position)) {
            return mHeaders[position].type
        } else if (isFooterViewPosition(position)) {
            return mFooters[position - getHeaderViewCount() - getNormalViewCount()].type
        }
        return mNormals[position - getHeaderViewCount()].type
    }

    override fun getItemCount(): Int {
        return getHeaderViewCount() + getNormalViewCount() + getFooterViewCount()
    }

    /*************** 创建ViewHolder *************/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (isHeaderType(viewType)) {
            return onCreateHeaderViewHolder(parent, viewType)!!
        } else if (isFooterType(viewType)) {
            return onCreateFooterViewHolder(parent, viewType)!!
        }

        return onCreateNormalViewHolder(parent, viewType)
    }

    abstract fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    protected open fun onCreateFooterViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        return null
    }

    protected open fun onCreateHeaderViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        return null
    }

    /**
     * 判断是否为头部类型
     */
    private fun isHeaderType(viewType: Int): Boolean {
        return mHeaders.takeIf { !it.isNullOrEmpty() }
            ?.firstOrNull { it.type == viewType } != null
    }

    private fun isFooterType(viewType: Int): Boolean {
        return mFooters.takeIf { !it.isNullOrEmpty() }
            ?.firstOrNull { it.type == viewType } != null
    }

    /*************** 绑定viewHolder ******************/
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            isHeaderViewPosition(position) -> {
                onBindHeaderViewHolder(holder, position)
            }
            isFooterViewPosition(position) -> {
                onBindFooterViewHolder(holder, position - getHeaderViewCount() - getNormalViewCount())
            }
            else -> {
                onBindNormalViewHolder(holder, position - getHeaderViewCount())
            }
        }
    }

    protected open fun onBindFooterViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    abstract fun onBindNormalViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    protected open fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    private fun isHeaderViewPosition(position: Int): Boolean {
        return getHeaderViewCount() > position
    }

    private fun isFooterViewPosition(position: Int): Boolean {
        return getNormalViewCount() + getHeaderViewCount() <= position
    }

    /*************开放方法**************/
    fun getHeaderViewCount() = mHeaders.size
    fun getFooterViewCount() = mFooters.size
    fun getNormalViewCount() = mNormals.size

    fun addHeaders(data: List<Any>) {
        data.forEach {
            addHeader(it)
        }
    }

    fun addHeader(data: Any) {
        addHeader(data, ITEM_TYPE_HEADER)
    }

    fun addHeader(data: Any, type: Int) {
        mHeaders.add(DataBean(data, type))
    }

    fun addFooters(data: List<Any>) {
        data.forEach {
            addFooter(it)
        }
    }

    fun addFooter(data: Any = Unit) {
        addFooter(data, ITEM_TYPE_FOOTER)
    }

    fun addFooter(data: Any, type: Int) {
        mFooters.add(DataBean(data, type))
    }

    fun addNormals(data: List<Any>) {
        data.forEach {
            addNormal(it)
        }
    }

    fun addNormal(data: Any) {
        addNormal(data, ITEM_TYPE_NORMAL)
    }

    fun addNormal(data: Any, type: Int) {
        mNormals.add(DataBean(data, type))
    }

    fun setHeaderOrFooterFullSpan(needFullSpan: Boolean) {
        this.needFullSpan = needFullSpan
    }

    /**********兼容gridLayout************/
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (needFullSpan && layoutManager is GridLayoutManager) {
            layoutManager as GridLayoutManager
            // 原本的跨列数计算方式
            val spanSizeLookup = layoutManager.spanSizeLookup
            // 新的跨列数计算方式
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    // 如果是头部或者尾部,设置一行的全部列数
                    if (isHeaderViewPosition(position) || isFooterViewPosition(position)) {
                        return layoutManager.spanCount
                    }
                    // 如果原来有设置计算方式,不破坏原来的方式
                    if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position)
                    }
                    // 如果原来没有计算方式
                    return 1
                }
            }
        }
    }

    /***************兼容staggeredGridLayout****************/
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val layoutParams = holder.itemView.layoutParams
        if (needFullSpan && layoutParams != null && layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            val position = holder.layoutPosition
            if (isHeaderViewPosition(position) || isFooterViewPosition(position)) {
                layoutParams.isFullSpan = true
            }
        }
    }

    protected data class DataBean(val data: Any, val type: Int)
}