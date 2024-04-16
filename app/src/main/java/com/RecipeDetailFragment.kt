package com.example.tastebudzz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Callback
import okhttp3.Call
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class RecipeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_detail, container, false)
        val foodName = arguments?.getString("foodName") ?: ""
        fetchRecipeDetails(foodName)
        return view
    }

    private fun fetchRecipeDetails(foodName: String) {
        Thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/complexSearch?query=$foodName&number=1")
                    .get()
                    .addHeader("X-RapidAPI-Key", "5ad5e6455amshcda3c41c76d60b2p13802fjsn46813a591e")
                    .addHeader("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val responseData = response.body()?.string()
                    if (responseData != null) {
                        Log.d("RecipeDetails", "Response Data: $responseData")
                        // Here you can convert the response string to a JSON object if needed, for now just as it is
                        // and log specific values
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("RecipeDetails", "Error fetching recipe details", e)
                // Handle the error
            }
        }.start()
    }
}
