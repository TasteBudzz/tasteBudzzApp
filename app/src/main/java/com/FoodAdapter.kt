package com

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tastebudzz.R
import com.example.tastebudzz.RecipeDetailActivity
import com.example.tastebudzz.RestaurantDetailActivity
import com.google.android.material.card.MaterialCardView

class FoodAdapter(
    private val context: Context,
    private val restaurant: Restaurant,
    private val foodItems: List<FoodItem>,
    private val onItemClick: (String, Boolean) -> Unit
) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentItem = foodItems[position]

        holder.foodImageView.setImageResource(currentItem.imageResId)
        holder.foodNameTextView.text = currentItem.name
        holder.priceTextView.text = currentItem.price


        holder.itemView.setOnClickListener {
            onItemClick(currentItem.name, false)
        }

        holder.parentVew.setOnClickListener {
            val intent = Intent(context, RecipeDetailActivity::class.java)
            intent.putExtra("FOOD_NAME", currentItem.name)
            intent.putExtra("RESTAURANT", restaurant)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = foodItems.size

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parentVew: MaterialCardView = itemView.findViewById(R.id.parent_food)
        val foodImageView: ImageView = itemView.findViewById(R.id.imageFood)
        val foodNameTextView: TextView = itemView.findViewById(R.id.textFoodName)
        val priceTextView: TextView = itemView.findViewById(R.id.textPrice)
    }
}
