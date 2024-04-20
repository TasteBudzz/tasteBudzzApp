package com.example.tastebudzz.data

import android.content.Intent
import com.example.tastebudzz.R
import android.os.Bundle
import android.widget.Button
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
import com.bumptech.glide.Glide
import com.example.tastebudzz.HomeActivity
import com.example.tastebudzz.RestaurantDetailActivity
import com.example.tastebudzz.SignInActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ReviewDetailActivity : AppCompatActivity() {
    private lateinit var nameView: TextView
    private lateinit var ratingView: RatingBar
    private lateinit var reviewView: TextView
    private lateinit var deleteButton: Button
    private  lateinit var restaurantImage: ImageView
    private lateinit var editButton: Button
    private lateinit var databaseReference: DatabaseReference
    private  lateinit var restaurant: Restaurant
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.view_review)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // initialize views and buttons
        nameView = findViewById(R.id.reviewRestaurantName)
        ratingView = findViewById(R.id.restaurantRatingBar)
        reviewView = findViewById(R.id.restaurantReview)
        deleteButton = findViewById(R.id.restaurantReviewSubmitButton)
        restaurantImage = findViewById(R.id.restaurantImage)
        editButton = findViewById(R.id.restaurantReviewCancelButton)

        // get restaurant
        val review = intent.getSerializableExtra("REVIEW") as Review
        reviewView.text = review.comment
        val ratingValue = (review.rating as Number).toFloat()
        ratingView.rating = ratingValue
        databaseReference = Firebase.database.reference
        var restaurant = databaseReference.child("restaurants").child(review.restaurantId.toString()).get()
            .addOnSuccessListener {
                val restaurantFirebase = it.value
                nameView.text = it.child("name").value.toString()
                Glide.with(this)
                    .load(it.child("restaurantImageURL").value.toString())
                    .fitCenter()
                    .into(restaurantImage)

            }

        editButton.setOnClickListener {
            val intent = Intent(this, ReviewEditActivity::class.java)
            intent.putExtra("REVIEW", review)
            startActivity(intent)
        }

        var confirmDelete = false
        // submit listner
        deleteButton.setOnClickListener {
            if (confirmDelete) {
                databaseReference.child("reviews").child(review.reviewId).removeValue()
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("DEST", 3)
                startActivity(intent)
            } else {
                deleteButton.text = "Confirm Delete"
                Toast.makeText(
                    baseContext,
                    "Are you sure you want to delete this review. If so, select the delete button.",
                    Toast.LENGTH_SHORT,
                ).show()
                confirmDelete = true
            }
        }

        findViewById<ImageView>(R.id.backBUtton).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("DEST", 3)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.logoutButton).setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }
}