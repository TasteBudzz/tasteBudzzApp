package com

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tastebudzz.R
import com.example.tastebudzz.RestaurantDetailActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val TAG = "RestaurantAdapter"

class RestaurantAdapter(private val context: Context, private val restaurants: List<Restaurant>) :
    RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.bind(restaurant)
    }

    override fun getItemCount() = restaurants.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val imageView = itemView.findViewById<ImageView>(R.id.restaurantImage)
        private val nameView = itemView.findViewById<TextView>(R.id.restaurantName)
        private val descriptionView = itemView.findViewById<TextView>(R.id.description)
        private val ratingView= itemView.findViewById<TextView>(R.id.rating)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(restaurant: Restaurant) {

            nameView.text = restaurant.name
            descriptionView.text = restaurant.description
            ratingView.text = "${restaurant.rating} out of 5.0"
            Glide.with(context)
                .load(restaurant.restaurantImageURL)
                .fitCenter()
                .into(imageView)
        }

        override fun onClick(v: View?) {
            // Get selected restuarnant
            val restaurant = restaurants[absoluteAdapterPosition]

            // write restuaurat to firebase db
            val database = Firebase.database.reference

            database.child("restaurants").child(restaurant.id.toString()).setValue(restaurant)

            // Navigate to Details screen and pass selected restaurant
            val intent = Intent(context, RestaurantDetailActivity::class.java)
            intent.putExtra("RESTAURANT", restaurant)
            context.startActivity(intent)
        }
    }
}