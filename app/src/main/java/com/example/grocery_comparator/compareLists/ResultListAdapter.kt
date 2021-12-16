package com.example.grocery_comparator.compareLists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery_comparator.R
import java.lang.IllegalArgumentException

class ResultListAdapter(var dataItem: MutableList<PricedItemUI>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_priced_list, parent, false)
        return PricedViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataItem[position]
        when (holder) {
            is PricedViewHolder -> holder.bind(item)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int = dataItem.size

    inner class PricedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val productText = itemView.findViewById<TextView>(R.id.priced_item)
        private val priceText = itemView.findViewById<TextView>(R.id.price)
        private val priceMaxiText = itemView.findViewById<TextView>(R.id.priceMaxi)

        private lateinit var item: PricedItemUI


        fun bind(item: PricedItemUI) {
            this.item = item
            productText.text = item.item_name
            priceText.text = item.price_i
            priceMaxiText.text = item.price_m
        }

    }

}