package com.example.tastebudzz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.FoodAdapter
import com.FoodItem
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import com.Constants.WORLDWIDE_RESTAURANTS_SEARCH_KEY
import com.Restaurant
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MenuActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodAdapter
    private lateinit var shimmer: ShimmerFrameLayout
    private  lateinit var restaurantImage: ImageView
    private  lateinit var selectedRestaurant: Restaurant
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        selectedRestaurant = intent.getSerializableExtra("RESTAURANT") as Restaurant

        // Retrieve the name and ID of the restaurant from the intent extras
        val restaurantName = selectedRestaurant.name
        val restaurantId =selectedRestaurant.id
        val textViewRestaurantName: TextView = findViewById(R.id.textViewRestaurantName)
        textViewRestaurantName.text = "${restaurantName}'s Menu"

        restaurantImage = findViewById(R.id.restaurantImage)
        Glide.with(this)
            .load(selectedRestaurant.restaurantImageURL)
            .fitCenter()
            .into(restaurantImage)

        // You can use restaurantId in your logic if needed
        Log.d("MenuActivity", "Restaurant Name : $restaurantName")
        Log.d("MenuActivity", "Restaurant ID: $restaurantId")

        recyclerView = findViewById(R.id.menuRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<ImageView>(R.id.backBUtton).setOnClickListener {
            Log.d("TOP_NAV", "Cliked back")

            val intent = Intent(this, RestaurantDetailActivity::class.java)
            intent.putExtra("RESTAURANT", selectedRestaurant)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.logoutButton).setOnClickListener{
            Log.d("TOP_NAV", "Cliked log out")
            Firebase.auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        shimmer = findViewById(R.id.shimmer_view)
        shimmer.setVisibility(View.VISIBLE);
        shimmer.startShimmer();
        // Call the function to fetch food items from API
        fetchFoodItems(this, restaurantId)
    }

    // Function to fetch food items from the API based on restaurant ID
    private fun fetchFoodItems(context: Context, restaurantId: Int) {
        val client = OkHttpClient()

        val mediaType = MediaType.parse("application/x-www-form-urlencoded")
        val body = RequestBody.create(
            mediaType, "currency=USD&language=en_US&location_id=$restaurantId"
        )
        val request =
            Request.Builder().url("https://worldwide-restaurants.p.rapidapi.com/detail").post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("X-RapidAPI-Key", WORLDWIDE_RESTAURANTS_SEARCH_KEY)
                .addHeader("X-RapidAPI-Host", "worldwide-restaurants.p.rapidapi.com").build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
                Log.e("MenuActivity", "Failed to fetch food items: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body()?.use { responseBody ->
                    val responseData = responseBody.string()
                    Log.d("MenuActivity", "Response Data: $responseData")
                    try {
                        // Parse the JSON response
                        val jsonObject = JSONObject(responseData)
                        val results = jsonObject.getJSONObject("results")
                        val dishesArray = results.getJSONArray("dishes")

                        if (dishesArray.length() > 0) {
                            // Extract food item names from the dishes array
                            val foodItems = mutableListOf<FoodItem>()
                            for (i in 0 until dishesArray.length()) {
                                val dish = dishesArray.getJSONObject(i)
                                val dishName = dish.optString("name", "")
                                foodItems.add(
                                    FoodItem(
                                        dishName,
                                        "",
                                        R.drawable.placeholder_food_image
                                    )
                                ) // You can add other details if needed
                            }

                            // Log the parsed food items
                            Log.d("MenuActivity", "Parsed Food Items: $foodItems")

                            // Update UI on the main thread
                            runOnUiThread {
                                // Initialize the adapter with fetched food items
                                adapter = FoodAdapter(context,selectedRestaurant, foodItems) { foodName, isRecipeClick ->
                                    if (isRecipeClick) {
                                        fetchRecipeForFood(foodName)
                                    } else {
                                        Log.d("MenuActivity", "Food item clicked: $foodName")
                                        // Handle normal food item click, maybe show details
                                    }
                                }
                                recyclerView.adapter = adapter

                                // Notify the adapter that the data set has changed
                                adapter.notifyDataSetChanged()
                                if(shimmer.isShimmerVisible())
                                {
                                    shimmer.stopShimmer();
                                    shimmer.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            Log.d("MenuActivity", "No dishes found in the response.")
                        }
                    } catch (e: Exception) {
                        // Handle parsing exception
                        Log.e("MenuActivity", "Error parsing response: ${e.message}")
                    }
                }
            }
        })
    }
}

private fun fetchRecipeForFood(foodName: String) {
    Log.d("MenuActivity", "Fetch recipe for: $foodName")
}