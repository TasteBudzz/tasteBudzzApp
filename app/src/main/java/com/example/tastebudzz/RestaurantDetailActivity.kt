package com.example.tastebudzz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Restaurant
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
    private lateinit var allReviewsButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_restaurant_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val selectedRestaurant = intent.getSerializableExtra("RESTAURANT") as Restaurant

        Log.v("TEST", selectedRestaurant.toString())
        nameView = findViewById(R.id.restaurantName)
        imageView = findViewById(R.id.restaurantImage)
        ratingView = findViewById(R.id.restaurantRating)
        rankingView = findViewById(R.id.restaurantRanking)
        cuisinesView = findViewById(R.id.restaurantCuisines)
        desscriptionView = findViewById(R.id.restaurantDescription)
        addReviewButton = findViewById(R.id.reviewButton)
        menuButton = findViewById(R.id.menuButton)
        allReviewsButton = findViewById(R.id.reviewsListButton)
        // set values
        nameView.text = selectedRestaurant.name
        Glide.with(this)
            .load(selectedRestaurant.restaurantImageURL)
            .fitCenter()
            .into(imageView)
        ratingView.text = "Rating: ${selectedRestaurant.rating}/5"
        rankingView.text = selectedRestaurant.ranking
        var restaurantCuisines = ""
        for (j in 0 until selectedRestaurant.cuisines.size) {
            var cuisine = selectedRestaurant.cuisines[j]
            if (j == selectedRestaurant.cuisines.size - 1) {
                restaurantCuisines += "and " + cuisine
            } else {
                restaurantCuisines += cuisine + ", "
            }
        }
        Log.v("TEST", restaurantCuisines)
        cuisinesView.text = restaurantCuisines
        desscriptionView.text = selectedRestaurant.description

        menuButton.setOnClickListener {
            // Navigate to menu items
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("RESTAURANT", selectedRestaurant)
            startActivity(intent)
        }
        addReviewButton.setOnClickListener {
            // Navigate to Details screen and pass selected restaurant
            val intent = Intent(this, RestaurantReviewActivity::class.java)
            intent.putExtra("RESTAURANT", selectedRestaurant)
            startActivity(intent)
        }

        allReviewsButton.setOnClickListener {
            val intent = Intent(this, RestaurantReviewsListActivity::class.java)
            intent.putExtra("RESTAURANT", selectedRestaurant)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.backBUtton).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.logoutButton).setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }


    }
}