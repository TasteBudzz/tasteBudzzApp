
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.Recipe
import com.SavedRecipesFragment
import com.example.tastebudzz.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RecipeDetailFragment : Fragment() {

    private lateinit var recipeNameTextView: TextView
    private lateinit var ingredientsEditText: EditText
    private lateinit var instructionsEditText: EditText
    private lateinit var nutritionTextView: TextView
    private lateinit var editRecipeButton: Button
    private lateinit var deleteRecipeButton: Button

    private var recipe: Recipe? = null
    private var isEditingMode = false
    private var confirmDelete = false

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
        deleteRecipeButton = view.findViewById(R.id.deleteRecipeButton)


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
        // Delete the recipe
        deleteRecipeButton.setOnClickListener {
            if (confirmDelete) {
                deleteRecipe()
            } else {
                confirmDelete = true
                deleteRecipeButton.text = "Confirm Delete"
                // You can show a confirmation dialog here if needed
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
            // Update the recipe details with edited values
            val editedRecipe = Recipe(
                it.id,
                it.userId,
                it.recipeId,
                it.name,
                it.recipeImageURL,
                it.nutritionInformation,
                ArrayList(ingredientsEditText.text.split("\n")), // Convert MutableList to ArrayList
                instructionsEditText.text.toString(),
                it.restaurantName
            )

            // Get reference to the Firebase Database
            val database = Firebase.database
            val userId = Firebase.auth.currentUser?.uid ?: return // Get the current user's ID
            val recipeRef = database.reference.child("recipes").child(editedRecipe.id)

            // Update the recipe details in Firebase
            recipeRef.setValue(editedRecipe)
                .addOnSuccessListener {
                    Log.d("RecipeDetailFragment", "Recipe details updated successfully in Firebase")
                }
                .addOnFailureListener { e ->
                    Log.e("RecipeDetailFragment", "Error updating recipe details in Firebase", e)
                }
        }
    }

    private fun deleteRecipe() {
        recipe?.let {
            // Get reference to the Firebase Database
            val database = Firebase.database
            val userId = Firebase.auth.currentUser?.uid ?: return // Get the current user's ID
            val recipeRef = database.reference.child("recipes").child(it.id)

            // Delete the recipe from Firebase
            recipeRef.removeValue()
                .addOnSuccessListener {
                    Log.d("RecipeDetailFragment", "Recipe deleted successfully from Firebase")
                    // Handle navigation back to previous page
                    // For example:
                    // Navigate back to the previous fragment
                    navigateBack()

                }
                .addOnFailureListener { e ->
                    Log.e("RecipeDetailFragment", "Error deleting recipe from Firebase", e)
                }
        }
    }

    private fun navigateBack() {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            // Replace the current fragment with the previous one
            replace(R.id.restaurant_frame_layout, SavedRecipesFragment())
            // Add the transaction to the back stack
            addToBackStack(null)
            // Commit the transaction
            commit()
        }
    }
}
