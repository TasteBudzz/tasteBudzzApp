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

    private var recipe: Recipe? = null
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

        // Retrieve the Recipe object from arguments
        recipe = arguments?.getSerializable("recipe") as? Recipe ?: Recipe(
            "",
            "",
            "",
            "",
            null,
            arrayListOf(),
            arrayListOf(),
            "",
            ""
        )
        populateRecipeDetails(recipe)

        // Display the details of the recipe
        editRecipeButton.setOnClickListener {
            if (isEditingMode) {
                saveRecipe()
            } else {
                enterEditingMode()
            }
        }

        return view
    }

    private fun populateRecipeDetails(recipe: Recipe?) {
        recipe?.let {
            this.recipe = it
            recipeNameTextView.text = it.name
            ingredientsEditText.setText(it.ingredients.joinToString("\n"))
            instructionsEditText.setText(it.instructions)
            nutritionTextView.text = it.nutritionInformation.joinToString("\n")
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

        recipe?.let {
            val editedRecipe = Recipe(
                it.id,
                it.userId,
                it.recipeId,
                it.name,
                it.recipeImageURL,
                it.nutritionInformation,
                ingredientsEditText.text.split("\n").toCollection(ArrayList()),
                instructionsEditText.text.toString(),
                it.restaurantName
            )
            // Log the edited recipe
            Log.d("RecipeDetailFragment", "Edited Recipe: $editedRecipe")
            // Now you can do whatever you need with the edited recipe, like save it to a database
        }
    }
}
