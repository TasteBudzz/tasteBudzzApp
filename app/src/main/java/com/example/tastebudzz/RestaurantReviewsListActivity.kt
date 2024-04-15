package com.example.tastebudzz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Restaurant
import com.RestaurantAdapter
import com.Review
import com.ReviewsAdapter
import com.example.tastebudzz.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.Serializable

class RestaurantReviewsListActivity : AppCompatActivity() {

    private lateinit var reviews: ArrayList<Review>
    private lateinit var reviewsRecyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewsAdapter
    private  lateinit var restaurant: Restaurant
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("DB", "Firebase DB trying to access")

        enableEdgeToEdge()
        setContentView(R.layout.activity_restaurant_reviews_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        reviews = arrayListOf()

        reviewsRecyclerView = findViewById(R.id.reviews_recycler_view)
        reviewsRecyclerView.layoutManager = LinearLayoutManager(this)
        reviewsRecyclerView.setHasFixedSize(true)
        reviewAdapter = ReviewsAdapter(this, reviews)
        reviewsRecyclerView.adapter = reviewAdapter

        restaurant= intent.getSerializableExtra("RESTAURANT") as Restaurant


        findViewById<ImageView>(R.id.backBUtton).setOnClickListener {
            val intent = Intent(this, RestaurantDetailActivity::class.java)
            intent.putExtra("RESTAURANT", restaurant)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.logoutButton).setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }


        getReivewsData()
    }
    private fun getReivewsData() {
        dbRef = FirebaseDatabase.getInstance().getReference("reviews")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    reviews.clear()
                    for (snapshot in dataSnapshot.children) {

                        Log.e("DB", snapshot.value.toString())
                        val ratingValue = (snapshot.child("rating").value as Number).toDouble()
                        val timestampValue = (snapshot.child("timestamp").value as Number).toLong()
                        val review = Review(
                            snapshot.child("userId").value.toString(),

                            snapshot.child("restaurantId").value.toString(),
                            ratingValue,
                            snapshot.child("comment").value.toString(),
                            snapshot.child("reviewerName").value.toString(),
                            timestampValue,
                            )
                        reviews.add(review)
                        Log.e("DB", review.toString())
                    }
                    reviewAdapter.notifyDataSetChanged()
                    Log.e("DB", "Firebase DB Reivews Accessed Successfully")

                }



            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(
                    "DB",
                    "Firebase DB Reivews Accessed failed with .",
                    databaseError.toException()
                )
            }
        })
    }


}
