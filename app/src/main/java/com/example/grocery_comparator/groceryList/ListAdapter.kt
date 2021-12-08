package com.example.grocery_comparator.groceryList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery_comparator.R
import java.lang.IllegalArgumentException

class ListAdapter(var dataItems: MutableList<ProductItemUI>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.reclycer_list, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataItems[position]
        when (holder) {
            is ProductViewHolder -> holder.bind(item)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int = dataItems.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private lateinit var item: ProductItemUI

        private val productText = itemView.findViewById<TextView>(R.id.product_item)

        fun bind(item: ProductItemUI) {
            this.item = item
            productText.text = item.product
        }

    }

}
