package com

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tastebudzz.R

private const val TAG = "RestaurantAdapter"

class RestaurantAdapter(private val context: Context, private val restaurants: List<Restaurant>) :
    RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = restaurants[position]
        holder.bind(article)
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
            // Get selected article
            val article = restaurants[absoluteAdapterPosition]

            // Navigate to Details screen and pass selected article
            //val intent = Intent(context, RestaurantDetailActivity::class.java)
            //intent.putExtra(ARTICLE_EXTRA, article)
            //context.startActivity(intent)
        }
    }
}