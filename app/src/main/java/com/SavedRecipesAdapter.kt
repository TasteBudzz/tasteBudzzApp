package com

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tastebudzz.R

class SavedRecipesAdapter(
    private val savedRecipes: List<SavedRecipe>,
    private val onItemClick: (SavedRecipe) -> Unit
) :
    RecyclerView.Adapter<SavedRecipesAdapter.SavedRecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedRecipeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_saved_recipe, parent, false)
        return SavedRecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedRecipeViewHolder, position: Int) {
        val savedRecipe = savedRecipes[position]
        holder.bind(savedRecipe)
        holder.itemView.setOnClickListener { onItemClick(savedRecipe) }
    }

    override fun getItemCount(): Int {
        return savedRecipes.size
    }

    inner class SavedRecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recipeNameTextView: TextView = itemView.findViewById(R.id.recipeName)
        private val restaurantNameTextView: TextView = itemView.findViewById(R.id.restaurantName)

        fun bind(savedRecipe: SavedRecipe) {
            recipeNameTextView.text = savedRecipe.recipeName
            restaurantNameTextView.text = savedRecipe.restaurantName
        }
    }
}
