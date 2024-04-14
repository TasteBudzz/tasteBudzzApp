package com.example.tastebudzz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Restaurant
import com.bumptech.glide.Glide
import org.json.JSONObject

class RestaurantDetailActivity : AppCompatActivity() {
    private lateinit var nameView: TextView
    private lateinit var imageView: ImageView
    private lateinit var ratingView: TextView
    private lateinit var rankingView: TextView
    private lateinit var cuisinesView: TextView
    private lateinit var desscriptionView: TextView

    private lateinit var menuButton: Button
    private lateinit var addReviewButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_restaurant_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize all view properties
        nameView = findViewById(R.id.restaurantName)
        imageView = findViewById(R.id.restaurantImage)
        ratingView = findViewById(R.id.restaurantRating)
        rankingView = findViewById(R.id.restaurantRanking)
        cuisinesView = findViewById(R.id.restaurantCuisines)
        desscriptionView = findViewById(R.id.restaurantDescription)
        menuButton = findViewById(R.id.menuButton)
        addReviewButton = findViewById(R.id.reviewButton)

        val selectedRestaurant = intent?.getSerializableExtra("RESTAURANT") as? Restaurant
        if (selectedRestaurant != null) {
            Log.v("TEST", selectedRestaurant.toString())

            // Set values for all views
            nameView.text = selectedRestaurant.name
            Glide.with(this)
                .load(selectedRestaurant.restaurantImageURL)
                .fitCenter()
                .into(imageView)
            ratingView.text = "Rating: ${selectedRestaurant.rating}/5"
            rankingView.text = selectedRestaurant.ranking

            // Concatenate cuisines
            val restaurantCuisines = selectedRestaurant.cuisines.joinToString(", ")
            cuisinesView.text = restaurantCuisines

            desscriptionView.text = selectedRestaurant.description

            menuButton.setOnClickListener {
                // Create an intent to start MenuActivity
                val menuIntent = Intent(this@RestaurantDetailActivity, MenuActivity::class.java)
                // Pass the name of the restaurant as an extra
                menuIntent.putExtra("RESTAURANT_NAME", selectedRestaurant.name)
                // Start the MenuActivity
                startActivity(menuIntent)
            }
            addReviewButton.setOnClickListener {
                // TODO navigate to add review
            }
        } else {
            // Handle the case where selectedRestaurant is null
            Log.e("ERROR", "Selected restaurant is null")
            // For example, you can show test variables or placeholder data
            nameView.text = "Test Restaurant"
            ratingView.text = "Rating: 4/5"
            rankingView.text = "Ranked #10"
            cuisinesView.text = "Cuisines: Italian, Mexican"
            desscriptionView.text = "This is a test restaurant description."
            // You can also hide or disable buttons if needed
            menuButton.visibility = View.VISIBLE  // Show the menuButton
            addReviewButton.isEnabled = false  // Disable the addReviewButton

            // Set OnClickListener for menuButton to start MenuActivity with a placeholder restaurant name
            menuButton.setOnClickListener {
                // Create an intent to start MenuActivity
                val menuIntent = Intent(this@RestaurantDetailActivity, MenuActivity::class.java)
                // Pass a placeholder restaurant name as an extra
                menuIntent.putExtra("RESTAURANT_NAME", "Test Restaurant")
                // Start the MenuActivity
                startActivity(menuIntent)
            }
        }
    }




}