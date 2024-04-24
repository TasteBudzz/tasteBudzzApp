package com

import RecipeDetailFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tastebudzz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class SavedRecipesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SavedRecipesAdapter
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved_recipes, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        auth = Firebase.auth
        dbRef = FirebaseDatabase.getInstance().getReference("recipes")

        val userRecipesQuery = dbRef.orderByChild("userId").equalTo(auth.currentUser!!.uid)
        userRecipesQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val savedRecipes = mutableListOf<Recipe>()
                    for (snapshot in dataSnapshot.children) {
                        val recipe = snapshot.getValue(Recipe::class.java)
                        recipe?.let { savedRecipes.add(it) }
                    }
                    adapter.updateRecipes(savedRecipes)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("SavedRecipesFragment", "Error fetching recipes: $databaseError")
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val savedRecipes =
            mutableListOf<Recipe>() // Initially empty, will be updated when data is fetched
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
        recyclerView.adapter = adapter
    }
}
