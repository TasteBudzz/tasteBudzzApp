package com

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tastebudzz.R
import com.example.tastebudzz.data.ReviewDetailActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val TAG = "ReviewsAdapter"

class ReviewsAdapter(private val context: Context, private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)
    }

    override fun getItemCount() = reviews.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val timestampView = itemView.findViewById<TextView>(R.id.reviewTimestamp)
        private val nameView = itemView.findViewById<TextView>(R.id.reviewerName)
        private val reviewView = itemView.findViewById<TextView>(R.id.review)
        private val ratingView= itemView.findViewById<TextView>(R.id.rating)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(review: Review) {

            nameView.text = review.reviewerName
            reviewView.text = review.comment
            ratingView.text = "${review.rating} out of 5"

            val sdf = SimpleDateFormat("dd MMMM yyyy hh:mm a", Locale.getDefault())
            val date = Date(review.timestamp)
            timestampView.text = sdf.format(date)
        }

        override fun onClick(v: View?) {
            // Get selected review
            val review = reviews[absoluteAdapterPosition]

            // navigate to edit only if reviewer is user
            if (Firebase.auth.currentUser!!.uid == review.userId) {
                // Navigate to review details screen and pass selected restaurant
                val intent = Intent(context, ReviewDetailActivity::class.java)
                intent.putExtra("REVIEW", review)
                context.startActivity(intent)
            }
        }
    }
}