package com

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tastebudzz.R

class RecipeDetailFragment : Fragment() {

    private lateinit var recipeNameTextView: TextView
    private lateinit var restaurantNameTextView: TextView
    private lateinit var ingredientsTextView: TextView
    private lateinit var instructionsTextView: TextView
    private lateinit var nutritionTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_detail, container, false)
        recipeNameTextView = view.findViewById(R.id.recipeNameTextView)
//        restaurantNameTextView = view.findViewById(R.id.restaurantNameTextView)
        ingredientsTextView = view.findViewById(R.id.ingredientsList)
        instructionsTextView = view.findViewById(R.id.instructionsListText)
        nutritionTextView = view.findViewById(R.id.nutritionInfoList)

        // Retrieve the SavedRecipe object from arguments
        val savedRecipe = arguments?.getSerializable("savedRecipe") as? SavedRecipe

        // Display the details of the saved recipe
        savedRecipe?.let {
            recipeNameTextView.text = it.recipeName
//            restaurantNameTextView.text = it.restaurantName
            ingredientsTextView.text = it.ingredients.joinToString("\n")
            instructionsTextView.text = it.instructions
            nutritionTextView.text = it.nutrition
        }

        return view
    }
}
