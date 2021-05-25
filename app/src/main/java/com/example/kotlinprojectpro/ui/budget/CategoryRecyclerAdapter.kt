import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinprojectpro.R
import com.example.kotlinprojectpro.models.Category
import kotlinx.android.synthetic.main.categoriese_budget.view.*


class CategoryRecyclerAdapter(val list: ArrayList<Category>, val listener: (Category) -> Unit) :
    RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category) = with(itemView) {
            valueCategory.text = category.value.toString()
            categoryTitle.text = category.category
        }
    }

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val rootView = LayoutInflater.from(viewGroup.context).inflate(R.layout.categoriese_budget, viewGroup, false)
        return ViewHolder(rootView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(list[position])
    }


}
