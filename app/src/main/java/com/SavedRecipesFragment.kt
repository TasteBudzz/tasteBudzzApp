package com

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tastebudzz.R

class SavedRecipesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SavedRecipesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved_recipes, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Sample data
        val savedRecipes = listOf(
            Recipe(
                "1",
                "userId1",
                "recipeId1",
                "Pasta Recipe",
                null,
                arrayListOf("Nutrition info for Recipe 1"),
                arrayListOf("Ingredient A1", "Ingredient A2"),
                "Instructions for Recipe 1",
                "Rodizio's Restaurant"
            ),
            Recipe(
                "2",
                "userId2",
                "recipeId2",
                "Grandma's Pizza Recipe",
                null,
                arrayListOf("Nutrition info for Recipe 2"),
                arrayListOf("Ingredient B1", "Ingredient B2"),
                "Instructions for Recipe 2",
                "Luigi's Restaurant"
            )
            // Add more saved recipes as needed
        )

        adapter = SavedRecipesAdapter(savedRecipes) { recipe ->
            // Handle item click here
            Log.d("Recipe", "Clicked on: ${recipe.name}")
            Log.d("Recipe", "Restaurant: ${recipe.restaurantName}")
            Log.d("Recipe", "Ingredients: ${recipe.ingredients.joinToString(", ")}")
            Log.d("Recipe", "Instructions: ${recipe.instructions}")
            Log.d("Recipe", "Nutrition: ${recipe.nutritionInformation.joinToString(", ")}")
            val fragment = RecipeDetailFragment()
            val bundle = Bundle()
            bundle.putSerializable("recipe", recipe)
            fragment.arguments = bundle
            fragmentManager?.beginTransaction()?.replace(R.id.restaurant_frame_layout, fragment)
                ?.commit()
        }
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter
        return view
    }
}
