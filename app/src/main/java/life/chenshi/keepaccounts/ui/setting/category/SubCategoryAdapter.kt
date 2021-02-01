package life.chenshi.keepaccounts.ui.setting.category

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import life.chenshi.keepaccounts.databinding.ItemCategoryBinding

class SubCategoryAdapter(val data: List<String>) : RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryAdapter.SubCategoryViewHolder {

    }

    override fun onBindViewHolder(holder: SubCategoryAdapter.SubCategoryViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class SubCategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

    }

}