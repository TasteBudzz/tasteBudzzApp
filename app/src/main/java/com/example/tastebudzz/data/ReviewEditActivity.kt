package com.example.tastebudzz.data

import com.example.tastebudzz.HomeActivity
import com.example.tastebudzz.MenuActivity
import com.example.tastebudzz.R
import com.example.tastebudzz.RestaurantReviewActivity
import com.example.tastebudzz.RestaurantReviewsListActivity
import com.example.tastebudzz.SignInActivity
import android.content.Intent
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ReviewEditActivity : AppCompatActivity() {
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
        setContentView(R.layout.edit_review)
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
        var review = intent.getSerializableExtra("REVIEW") as Review
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
            databaseReference.child("reviews").child("")
        }

        var confirmDelete = false
        // submit listner
        deleteButton.setOnClickListener {
            if (confirmDelete) {
                val newRating = ratingView.rating
                val newComment = reviewView.text.toString()
                val newTimestamp = System.currentTimeMillis()
                val newReview =  Review(review.reviewId, review.userId, review.restaurantId, newRating.toDouble(), newComment, Firebase.auth.currentUser!!.displayName,
                    System.currentTimeMillis()
                )
                databaseReference.child("reviews").child(review.reviewId).child("rating").setValue(newRating)
                databaseReference.child("reviews").child(review.reviewId).child("comment").setValue(newComment)
                databaseReference.child("reviews").child(review.reviewId).child("timestamp").setValue(newTimestamp)
                val intent = Intent(this, ReviewDetailActivity::class.java)
                intent.putExtra("REVIEW", newReview)
                startActivity(intent)


            } else {
                deleteButton.text = "Confirm Save"
                Toast.makeText(
                    baseContext,
                    "Are you sure you want to save your edits. If so, select save again.",
                    Toast.LENGTH_SHORT,
                ).show()
                confirmDelete = true
            }
        }

        findViewById<ImageView>(R.id.backBUtton).setOnClickListener {
            val intent = Intent(this, ReviewDetailActivity::class.java)
            intent.putExtra("REVIEW", review)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.logoutButton).setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}