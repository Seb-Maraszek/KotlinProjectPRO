package com.example.kotlinprojectpro.ui.main

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinprojectpro.R
import com.example.kotlinprojectpro.models.Expense

class RecyclerViewAdapter(
    private val list: ArrayList<Any>,
    val listener: (Expense) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_HEADER = 1
    private val TYPE_ITEM = 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById<View>(R.id.titleExpense) as TextView
        val expenseIcon:ImageView = itemView.findViewById<View>(R.id.expenseIcon) as ImageView
        val category:TextView = itemView.findViewById<View>(R.id.categoryText) as TextView
        val expenseDate:TextView = itemView.findViewById<View>(R.id.expenseDate) as TextView
        val value:TextView = itemView.findViewById<View>(R.id.value) as TextView
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val saving: TextView = itemView.findViewById<View>(R.id.savings) as TextView
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is String -> TYPE_HEADER
            is Expense -> TYPE_ITEM
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM -> {
                val itemView: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.expense_item, parent, false)
                ViewHolder(itemView)
            }
            TYPE_HEADER -> {
                val itemView: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_header, parent, false)
                HeaderViewHolder(itemView)
            }
            else -> throw IllegalArgumentException("Invalid viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder){
            holder.saving.text = list[position] as String
        } else if(holder is ViewHolder){
            val item = list[position]
            if(item is Expense) {
                if(position > 2) {
                    val prevItem = list[position - 1] as Expense
                    if(item.date == prevItem.date) {
                        holder.expenseDate.visibility = View.GONE
                    } else {
                        holder.expenseDate.text = item.date
                    }
                } else {
                    holder.expenseDate.text = item.date
                }
                holder.expenseIcon.setImageURI(Uri.parse(item.image))
                holder.title.text = item.title
                holder.category.text = item.category
                holder.value.text = item.value
            }
        }
    }
}