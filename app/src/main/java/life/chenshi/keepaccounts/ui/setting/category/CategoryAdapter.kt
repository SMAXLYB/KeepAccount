package life.chenshi.keepaccounts.ui.setting.category

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import life.chenshi.keepaccounts.databinding.ItemCategoryBinding

class CategoryAdapter(val data:List<String>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

    }

}