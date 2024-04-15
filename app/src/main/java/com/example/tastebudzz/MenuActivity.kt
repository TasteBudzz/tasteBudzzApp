package com.example.tastebudzz

import android.os.Bundle
import android.util.Log
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

class MenuActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Retrieve the name and ID of the restaurant from the intent extras
        val restaurantName = intent.getStringExtra("RESTAURANT_NAME")
        val restaurantId = intent.getIntExtra("RESTAURANT_ID", -1) // Default value -1 if not found
        val textViewRestaurantName: TextView = findViewById(R.id.textViewRestaurantName)
        textViewRestaurantName.text = restaurantName

        // You can use restaurantId in your logic if needed
        Log.d("MenuActivity", "Restaurant Name : $restaurantName")
        Log.d("MenuActivity", "Restaurant ID: $restaurantId")

        recyclerView = findViewById(R.id.menuRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Call the function to fetch food items from API
        fetchFoodItems(restaurantId)
    }

    // Function to fetch food items from the API based on restaurant ID
    private fun fetchFoodItems(restaurantId: Int) {
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
                                adapter = FoodAdapter(foodItems) { foodName ->
                                    // TODO: Handle item click here, for example, navigate to the next screen
                                    Log.d("MenuActivity", "Clicked food item: $foodName")
                                }
                                recyclerView.adapter = adapter

                                // Notify the adapter that the data set has changed
                                adapter.notifyDataSetChanged()
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
