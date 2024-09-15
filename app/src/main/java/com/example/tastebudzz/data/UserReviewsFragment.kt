package com.example.tastebudzz.data

import android.content.Intent
import com.Restaurant
import com.RestaurantAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.Review
import com.ReviewsAdapter
import com.example.tastebudzz.R
import com.example.tastebudzz.RestaurantDetailActivity
import com.example.tastebudzz.SignInActivity
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder

class UserReviewsFragment : Fragment() {

    private lateinit var reviews: ArrayList<Review>
    private lateinit var reviewsRecyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewsAdapter
    private  lateinit var restaurant: Restaurant
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_reviews, container, false)

        val layoutManager = LinearLayoutManager(context)
        reviews = arrayListOf()

        reviewsRecyclerView = view.findViewById(R.id.reviews_recycler_view)
        reviewsRecyclerView.layoutManager = layoutManager
        reviewsRecyclerView.setHasFixedSize(true)
        reviewAdapter = ReviewsAdapter(view.context, reviews)
        reviewsRecyclerView.adapter = reviewAdapter


        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("REVIEWS", "fetching user reviews")
        getReivewsData()
    }


    private fun getReivewsData() {
        dbRef = FirebaseDatabase.getInstance().getReference("reviews")
        val restaurantReviews = dbRef.orderByChild("userId").equalTo(Firebase.auth.currentUser!!.uid.toString())
        Log.e("REVIEWS", "fetching user reviews for ${Firebase.auth.currentUser!!.uid.toString()}")

        restaurantReviews.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    reviews.clear()
                    for (snapshot in dataSnapshot.children.reversed()) {
                        Log.e("DB", snapshot.value.toString())
                        val ratingValue = (snapshot.child("rating").value as Number).toDouble()
                        val timestampValue =
                            (snapshot.child("timestamp").value as Number).toLong()
                        val review = Review(
                            snapshot.child("reviewId").value.toString(),
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