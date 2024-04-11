package com.example.tastebudzz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MenuActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val restaurantName = "Your Restaurant Name" // Replace this with actual restaurant name
        val textViewRestaurantName: TextView = findViewById(R.id.textViewRestaurantName)
        textViewRestaurantName.text = restaurantName

        recyclerView = findViewById(R.id.menuRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FoodAdapter(getFoodItems()) { foodName ->
            // Handle item click here, for example, navigate to the next screen
//            val intent = Intent(this, NextActivity::class.java)
//            intent.putExtra("foodName", foodName)
//            startActivity(intent)
            // Log the clicked food item name
            Log.d("MenuActivity", "Clicked food item: $foodName")
        }
        recyclerView.adapter = adapter
    }

    // Function to generate dummy food items (Replace this with actual API call)
    private fun getFoodItems(): List<FoodItem> {
        val foodItems = mutableListOf<FoodItem>()
        for (i in 1..10) { // Generate 10 dummy food items
            foodItems.add(FoodItem("Food $i", "$${(i * 2)}.00", R.drawable.placeholder_food_image))
        }
        return foodItems
    }
}
