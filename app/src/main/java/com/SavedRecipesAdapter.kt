package com

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tastebudzz.R

class SavedRecipesAdapter(
    private val recipes: List<Recipe>,
    private val onItemClick: (Recipe) -> Unit
) :
    RecyclerView.Adapter<SavedRecipesAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_saved_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
        holder.itemView.setOnClickListener { onItemClick(recipe) }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recipeNameTextView: TextView = itemView.findViewById(R.id.recipeName)
        private val restaurantNameTextView: TextView = itemView.findViewById(R.id.restaurantName)

        fun bind(recipe: Recipe) {
            recipeNameTextView.text = recipe.name
            restaurantNameTextView.text = recipe.restaurantName
        }
    }
}
