package com

import android.os.Bundle
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
            SavedRecipe(
                "Pasta Recipe",
                "Rodizio's Restaurant",
                listOf("Ingredient A1", "Ingredient A2"),
                "Instructions for Recipe 1",
                "Nutrition info for Recipe 1"
            ),
            SavedRecipe(
                "Grandma's Pizza Recipe",
                "Luigis Restaurant",
                listOf("Ingredient B1", "Ingredient B2"),
                "Instructions for Recipe 2",
                "Nutrition info for Recipe 2"
            )
            // Add more saved recipes as needed
        )

        adapter = SavedRecipesAdapter(savedRecipes) { savedRecipe ->
            // Handle item click here, e.g., navigate to the specific recipe page
        }
        recyclerView.adapter = adapter
        return view
    }
}
