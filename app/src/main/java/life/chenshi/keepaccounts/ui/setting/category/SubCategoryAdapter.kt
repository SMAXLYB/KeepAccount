package life.chenshi.keepaccounts.ui.setting.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import life.chenshi.keepaccounts.databinding.ItemSubCategoryBinding

class SubCategoryAdapter(val data: List<String>) : RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryAdapter.SubCategoryViewHolder {
        val binding = ItemSubCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SubCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubCategoryAdapter.SubCategoryViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class SubCategoryViewHolder(val binding: ItemSubCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

    }

}