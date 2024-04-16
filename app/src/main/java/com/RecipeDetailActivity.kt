package com.example.tastebudzz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.Restaurant
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URLEncoder

class RecipeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_recipe_detail)

        val selectedRestaurant = intent.getSerializableExtra("FOOD_NAME") as? String
        fetchRecipeDetails(selectedRestaurant.toString())
    }

    private fun fetchRecipeDetails(foodName: String) {
        Thread {
            try {
                /*
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/complexSearch?query=${URLEncoder.encode(foodName.lowercase()).toString()}")
                    .get()
                    .addHeader("X-RapidAPI-Key", "5ad5e6455amshcda3c41c76d60b2p13802fjsn46813a591e")
                    .addHeader("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val responseData = response.body()?.string()
                    if (responseData != null) {
                        Log.d("RecipeDetails", "Response Data: $responseData")
                        // Here you can convert the response string to a JSON object if needed,
                        // and log specific values
                    }
                }*/
                val client = OkHttpClient()

                val request = Request.Builder()
                    .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/complexSearch?query=${URLEncoder.encode(foodName.lowercase()).toString()}")
                    .get()
                    .addHeader("X-RapidAPI-Key", "1de6516ce2mshdc6312d9d47f229p1036fejsn9fa66e182335")
                    .addHeader("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                    .build()

                val response = client.newCall(request).execute()
                Log.d("RecipeDetailsRESULT", "Response Data: ${response.body()!!.string()}")

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("RecipeDetails", "Error fetching recipe details", e)
                // Handle the error
            }
        }.start()
    }
}
