package com.example.tastebudzz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class RestaurantReviewsListActivity : AppCompatActivity() {

    private val reviews = mutableListOf<Review>()
    private lateinit var reviewsRecyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_restaurant_reviews_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        reviewsRecyclerView = findViewById(R.id.reviews_recycler_view)
        reviewAdapter =  ReviewsAdapter(this, reviews)
        reviewsRecyclerView.adapter = reviewAdapter

    }

}