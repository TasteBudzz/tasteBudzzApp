//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import com.SavedRecipe
//import com.example.tastebudzz.R
//
//class RecipeDetailsFragment : Fragment() {
//
//    // Placeholder for the selected recipe
//    private lateinit var selectedRecipe: SavedRecipe
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_recipe_details, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Set up click listener for the edit button
//        editButton.setOnClickListener {
//            // Handle edit button click event
//            // For now, let's just show a toast message
//            // Replace this with your own logic to handle editing the recipe
//            showToast("Edit button clicked")
//        }
//
//        // Check if a recipe is selected
//        if (::selectedRecipe.isInitialized) {
//            // Populate the UI with recipe details
//            recipeNameHeader.text = selectedRecipe.recipeName
//
//            // Populate ingredients
//            ingredientsHeader.text = "Ingredients"
//            ingredientsList.text = selectedRecipe.ingredients.joinToString("\n")
//
//            // Populate instructions
//            instructionsHeader.text = "Instructions"
//            instructionsText.text = selectedRecipe.instructions
//
//            // Populate nutrition information
//            nutritionHeader.text = "Nutrition"
//            nutritionInfo.text = selectedRecipe.nutrition
//        }
//    }
//
//    // Function to set the selected recipe
//    fun setSelectedRecipe(recipe: SavedRecipe) {
//        selectedRecipe = recipe
//    }
//
//    // Helper function to show a toast message
//    private fun showToast(message: String) {
//        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//    }
//}