package life.chenshi.keepaccounts.common.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseBottomSheetAdapter<T, V : ViewDataBinding>(private val date: List<T>) :
    RecyclerView.Adapter<BaseBottomSheetAdapter<T, V>.Holder>() {

    private var listener: ((V, T, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = DataBindingUtil.inflate<V>(
            LayoutInflater.from(parent.context),
            getResLayoutId(),
            parent,
            false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        onBindViewHolder(holder.binding, date[position])
    }

    abstract fun onBindViewHolder(binding: V, itemData: T)

    override fun getItemCount(): Int = date.size

    fun setOnItemClickListener(listener: (V, T, Int) -> Unit) {
        this.listener = listener
    }

    @LayoutRes
    abstract fun getResLayoutId(): Int

    inner class Holder(val binding: V) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener?.invoke(binding, date[bindingAdapterPosition], bindingAdapterPosition)
            }
        }
    }
}