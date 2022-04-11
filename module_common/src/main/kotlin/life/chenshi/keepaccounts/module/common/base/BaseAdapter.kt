package life.chenshi.keepaccounts.module.common.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, V : ViewDataBinding>(private var data: List<T>) :
    RecyclerView.Adapter<BaseAdapter<T, V>.Holder>() {

    private var listener: ((V, T, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = DataBindingUtil.inflate<V>(
            LayoutInflater.from(parent.context),
            setResLayoutId(),
            parent,
            false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        onBindViewHolder(holder.binding, data[position])
    }

    abstract fun onBindViewHolder(binding: V, itemData: T)

    override fun getItemCount(): Int = data.size

    fun setOnItemClickListener(listener: (V, T, Int) -> Unit) {
        this.listener = listener
    }

    fun getData(): List<T> {
        return data
    }

    fun setData(data:List<T>){
        this.data = data
        notifyDataSetChanged()
    }

    @LayoutRes
    abstract fun setResLayoutId(): Int

    inner class Holder(val binding: V) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener?.invoke(binding, data[bindingAdapterPosition], bindingAdapterPosition)
            }
        }
    }
}