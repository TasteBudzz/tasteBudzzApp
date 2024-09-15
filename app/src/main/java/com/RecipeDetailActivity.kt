package com.example.tastebudzz

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.Recipe
import com.Restaurant
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject

class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var recipeNameView: TextView
    private lateinit var recipeImageView: ImageView
    private lateinit var nutritionList: TextView
    private lateinit var restaurant: Restaurant
    private lateinit var foodName: String
    private lateinit var ingredientList: TextView
    private lateinit var recipeInfo: TextView
    private  lateinit var recipe: Recipe
    private lateinit var saveRecipeButton: Button
    private var cuisinesQuery = ""
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe_detail)

        foodName= intent.getSerializableExtra("FOOD_NAME") as String
        restaurant = intent.getSerializableExtra("RESTAURANT") as Restaurant

        recipeNameView = findViewById(R.id.recipeName)
        recipeImageView = findViewById(R.id.recipeImage)
        nutritionList = findViewById(R.id.recipeNutritionsInfo)
        ingredientList = findViewById(R.id.ingredientsList)
        recipeInfo = findViewById(R.id.recipeInfo)
        saveRecipeButton = findViewById(R.id.saveRecipeButton)
        shimmerFrameLayout = findViewById(R.id.shimmerView)

        findViewById<ImageView>(R.id.backBUtton).setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("RESTAURANT", restaurant)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.logoutButton).setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            if (it.itemId == R.id.action_recipe ) {
                Log.e("BOTTOM_NAV", "Selected your recipes screen")
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("DEST", 1)
                startActivity(intent)
            } else if (it.itemId == R.id.action_restaurant_search )
            {
                Log.e("BOTTOM_NAV", "Selected your restaurant screen")
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("DEST", 1)
                startActivity(intent)
            } else if (it.itemId == R.id.action_reviews ) {
                Log.e("BOTTOM_NAV", "Selected your reviews screen")
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("DEST", 3)
                startActivity(intent)
            }
            true
        }




        BackgroundFetchRestaurants().run()
    }

    inner class BackgroundFetchRestaurants: Runnable {
        override  fun run() {
            shimmerFrameLayout.startShimmer()
            fetchRecipeDetails(foodName, cuisinesQuery)
        }
    }


    private fun fetchRecipeDetails(foodName: String, cuisines: String) {
        Thread {
            try {
                val client = OkHttpClient()

                val request = Request.Builder()
                    .url(
                        "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/complexSearch?query=${foodName}&cuisine=${cuisines}&instructionsRequired=true&fillIngredients=true&addRecipeInformation=true&maxCarbs=1000000000&maxProtein=1000000000&maxCalories=1000000000&maxFat=1000000000&maxCholesterol=1000000000&maxSodium=1000000000&maxSugar=1000000000&number=1000000000&ranking=2"
                    )
                    .get()
                    .addHeader(
                        "X-RapidAPI-Key",
                        "X-RapidAPI-Key"
                    )
                    .addHeader(
                        "X-RapidAPI-Host",
                        "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com"
                    )
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body()!!.string()
                Log.v("API", responseBody)

                var jsonResponse = JSONObject(responseBody)
                var jsonRestaurants = jsonResponse.getJSONArray("results")
                val jsonRestaurant = JSONObject(jsonRestaurants[0].toString())
                val resId = jsonRestaurant.get("id").toString()
                Log.v("foodid: ", resId)
                val resName = jsonRestaurant.get("title")
                val resImg = jsonRestaurant.get("image")

                val detailRequest = Request.Builder()
                    .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/$resId/information")
                    .get()
                    .addHeader("X-RapidAPI-Key", "X-RapidAPI-Key")
                    .addHeader("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                    .build()

                val detailResponse = client.newCall(detailRequest).execute()
                val detailResponseBody = detailResponse.body()!!.string()
                Log.v("GET RECIPE INFORMATION API", responseBody)

                var readyInMinutes = 0
                var servings = 0
                var recipeSummary = ""
                var RecipeInstructions = ""
                var ingredientsList = ArrayList<String>()
                try {
                    if(detailResponseBody != null){
                        var jsonResponse2 = JSONObject(detailResponseBody)
                        readyInMinutes = jsonResponse2.getInt("readyInMinutes")
                        servings = jsonResponse2.getInt("servings")
                        recipeSummary = jsonResponse2.getString("instructions")
                        val ingredientsArray = jsonResponse2.getJSONArray("extendedIngredients")

                        for (i in 0 until ingredientsArray.length()) {
                            val ingredient = ingredientsArray.getJSONObject(i)
                            var ingredientName = ingredient.getString("name")
                            ingredientName += " " + ingredient.getInt("amount").toString() +" "+ ingredient.getString("unit")
                            ingredientsList.add(ingredientName)
                        }
                        Log.v("Ingredients", ingredientsList.toString())

                        val detailsBuilder = StringBuilder()
                        detailsBuilder.append("Preparation Time: $readyInMinutes minutes\n")
                        detailsBuilder.append("Servings: $servings\n")
                        detailsBuilder.append("Recipe: $recipeSummary\n")
                        Log.v("Recipe INFO", detailsBuilder.toString())
                        RecipeInstructions = detailsBuilder.toString()


                    }
                } catch (exception: JSONException) {
                    Log.e("Recipe API", "error fetching recipe details", exception)
                    if (detailResponseBody != null) {
                        Log.e("Recipe Detail Response Body", detailResponseBody)
                    }
                }

                var resNutrition = ArrayList<String>()
                val jsonNutrition =
                    jsonRestaurant.getJSONObject("nutrition").getJSONArray("nutrients")

                for (j in 0 until jsonNutrition.length()) {
                    val nutrition = JSONObject(jsonNutrition[j].toString())
                    val nutritionString = "${nutrition.get("name")}: ${nutrition.get("amount")} ${
                        nutrition.get(
                            "unit"
                        )
                    }"
                    resNutrition.add(nutritionString)
                }
                val databaseid = Firebase.database.reference.push().key.toString()
                val recipe = Recipe(
                    databaseid,
                    Firebase.auth.currentUser!!.uid,
                    resId,
                    resName as String,
                    resImg as String,
                    resNutrition,
                    ingredientsList,
                    RecipeInstructions,
                    restaurant.name.toString()
                )
                Log.e("RESTAURANT", recipe.toString())


            val updateUI = Runnable {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.hideShimmer()
                var nutritionText = ""
                for (i in 0 until recipe.nutritionInformation.size) {
                    nutritionText += recipe.nutritionInformation[i] + "\n"
                }
                /*
                //APPEND Recipe, Prep time, ingredients, Servings to nutritionText
                nutritionText += "\n--- Recipe Details ---\n"
                nutritionText += "Preparation Time: $readyInMinutes minutes\n"
                nutritionText += "Servings: $servings\n"
                nutritionText += "Ingredients:\n$ingredientsList\n"
                nutritionText += "Recipe:\n$recipeSummary\n"
                */

                recipeNameView.text = recipe.name
                var recipetxt = "Ingredients:\n"
                for (i in 0 until recipe.ingredients.size) {
                     recipetxt += "${i+1}. " + recipe.ingredients[i] + "\n"
                }
                ingredientList.text = recipetxt
                recipeInfo.text = recipe.instructions
                Glide.with(this)
                    .load(recipe.recipeImageURL)
                    .fitCenter()
                    .into(recipeImageView)
                nutritionList.text = nutritionText
                saveRecipeButton.setOnClickListener {
                    // write restuaurat to firebase db
                    val database = Firebase.database.reference

                    database.child("recipes").child(recipe.id).setValue(recipe)
                    saveRecipeButton.isClickable = false
                    saveRecipeButton.isEnabled = false
                }

            }
            Handler(Looper.getMainLooper()).post((updateUI))

        } catch (e: Exception) {
                e.printStackTrace()
                Log.e("RecipeDetails", "Error fetching recipe details", e)
                // Handle the error
            }
        }.start()
    }
}
