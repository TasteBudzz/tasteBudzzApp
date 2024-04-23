package com

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.tastebudzz.R

class RecipeDetailFragment : Fragment() {

    private lateinit var recipeNameTextView: TextView
    private lateinit var ingredientsEditText: EditText
    private lateinit var instructionsEditText: EditText
    private lateinit var nutritionTextView: TextView
    private lateinit var editRecipeButton: Button

    private var savedRecipe: SavedRecipe? = null
    private var isEditingMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_detail, container, false)
        recipeNameTextView = view.findViewById(R.id.recipeNameTextView)
        ingredientsEditText = view.findViewById(R.id.ingredientsList)
        instructionsEditText = view.findViewById(R.id.instructionsListText)
        nutritionTextView = view.findViewById(R.id.nutritionInfoList)
        editRecipeButton = view.findViewById(R.id.editRecipeButton)

        // Retrieve the SavedRecipe object from arguments
        savedRecipe = arguments?.getSerializable("savedRecipe") as? SavedRecipe ?: SavedRecipe(
            "",
            "",
            emptyList(),
            "",
            ""
        )
        populateRecipeDetails(savedRecipe)

        // Display the details of the saved recipe
        editRecipeButton.setOnClickListener {
            if (isEditingMode) {
                saveRecipe()
            } else {
                enterEditingMode()
            }
        }

        return view
    }

    private fun populateRecipeDetails(savedRecipe: SavedRecipe?) {
        savedRecipe?.let {
            this.savedRecipe = it
            recipeNameTextView.text = it.recipeName
            ingredientsEditText.setText(it.ingredients.joinToString("\n"))
            instructionsEditText.setText(it.instructions)
            nutritionTextView.text = it.nutrition
        }
    }

    private fun enterEditingMode() {
        isEditingMode = true
        editRecipeButton.text = "Save Recipe"
        instructionsEditText.isEnabled = true
        ingredientsEditText.isEnabled = true
    }

    private fun saveRecipe() {
        isEditingMode = false
        editRecipeButton.text = "Edit Recipe"
        instructionsEditText.isEnabled = false
        ingredientsEditText.isEnabled = false

        savedRecipe?.let {
            val editedRecipe = SavedRecipe(
                it.recipeName,
                it.restaurantName,
                ingredientsEditText.text.split("\n"),
                instructionsEditText.text.toString(),
                it.nutrition
            )
            // Log the edited recipe
            Log.d("RecipeDetailFragment", "Saved Recipe: $editedRecipe")
            // Now you can do whatever you need with the edited recipe, like save it to a database
        }
    }
}
