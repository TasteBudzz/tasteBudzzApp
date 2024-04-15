package com.example.tastebudzz

import android.content.Intent
import android.media.Rating
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Restaurant
import com.Review
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import android.content.Context
import com.bumptech.glide.Glide
import java.sql.Time
import java.sql.Timestamp

class RestaurantReviewActivity : AppCompatActivity() {
    private lateinit var nameView: TextView
    private lateinit var ratingView: RatingBar
    private lateinit var reviewView: EditText
    private lateinit var reviewSubmitButton: Button
    private  lateinit var restaurantImage: ImageView
    private lateinit var cancelButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_restaurant_review)
       ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // initialize views and buttons
        nameView = findViewById(R.id.reviewRestaurantName)
        ratingView = findViewById(R.id.restaurantRatingBar)
        reviewView = findViewById(R.id.restaurantReview)
        reviewSubmitButton = findViewById(R.id.restaurantReviewSubmitButton)
        restaurantImage = findViewById(R.id.restaurantImage)
        cancelButton = findViewById(R.id.restaurantReviewCancelButton)

        // get restaurant
        val selectedRestaurant = intent.getSerializableExtra("RESTAURANT") as Restaurant

        //set view values
        nameView.text = selectedRestaurant.name
        Glide.with(this)
            .load(selectedRestaurant.restaurantImageURL)
            .fitCenter()
            .into(restaurantImage)
        var confirmReview = false
        //cancel listenr
        cancelButton.setOnClickListener {
            // Navigate to Details screen and pass selected restaurant
            val intent = Intent(this, RestaurantDetailActivity::class.java)
            intent.putExtra("RESTAURANT", selectedRestaurant)
            startActivity(intent)
        }


        findViewById<ImageView>(R.id.backBUtton).setOnClickListener {
            val intent = Intent(this, RestaurantDetailActivity::class.java)
            intent.putExtra("RESTAURANT", selectedRestaurant)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.logoutButton).setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }


        // submit listner
        reviewSubmitButton.setOnClickListener {
            var rating = ratingView.rating.toDouble()
            var review = reviewView.text.toString()
            var user_id = Firebase.auth.currentUser!!.uid
            val restaurantId = selectedRestaurant.id.toString()

            // field validation
            var validReview = true
            if (review.length == 0) {
                Toast.makeText(
                    baseContext,
                    "Review is empty please add a review",
                    Toast.LENGTH_SHORT,
                ).show()
                validReview = false
            }
            if (rating.toDouble() == 0.0 && !confirmReview ) {
                Toast.makeText(
                    baseContext,
                    "Your rating is currently 0. Submit again to confirm.",
                    Toast.LENGTH_SHORT,
                ).show()
                reviewSubmitButton.text = "Confirm Submit"
                validReview = false
                confirmReview = true
            }

            if (validReview) {
                // add review to db
                val database = Firebase.database.reference
                val reviewData = Review(user_id, restaurantId, rating, review, Firebase.auth.currentUser!!.displayName,
                    System.currentTimeMillis()
                )
                database.child("reviews").push().setValue(reviewData)
                // Navigate to Details screen and pass selected restaurant
                val intent = Intent(this, RestaurantDetailActivity::class.java)
                intent.putExtra("RESTAURANT", selectedRestaurant)
                startActivity(intent)
            }
        }
    }
}