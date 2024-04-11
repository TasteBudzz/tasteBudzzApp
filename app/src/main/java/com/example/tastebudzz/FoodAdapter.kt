package com.example.tastebudzz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(private val foodItems: List<FoodItem>, private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentItem = foodItems[position]

        holder.foodImageView.setImageResource(currentItem.imageResId)
        holder.foodNameTextView.text = currentItem.name
        holder.priceTextView.text = currentItem.price

        holder.itemView.setOnClickListener {
            onItemClick(currentItem.name)
        }
    }

    override fun getItemCount() = foodItems.size

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodImageView: ImageView = itemView.findViewById(R.id.imageFood)
        val foodNameTextView: TextView = itemView.findViewById(R.id.textFoodName)
        val priceTextView: TextView = itemView.findViewById(R.id.textPrice)
    }
}
