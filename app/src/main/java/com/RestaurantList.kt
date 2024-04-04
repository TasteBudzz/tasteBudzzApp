package com

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tastebudzz.R
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder


private const val TAG = "RestaurantList"
private const val SEARCH_API_KEY = "1de6516ce2mshdc6312d9d47f229p1036fejsn9fa66e182335"
private const val RESTAURANT_SEARCH_URL = "1de6516ce2mshdc6312d9d47f229p1036fejsn9fa66e182335"
private const val LOCATION_SEARCH_API_KEY= "5ade6a67874d9716be26e95bee91bd09c52eaeed"

class RestaurantList : Fragment() {

    private val restaurants = mutableListOf<Restaurant>()
    private lateinit var restaurantsRecyclerView: RecyclerView
    private lateinit var restaurantAdapter: RestaurantAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_restaurant_list, container, false)

        val layoutManager = LinearLayoutManager(context)
        restaurantsRecyclerView = view.findViewById(R.id.article_recycler_view)
        restaurantsRecyclerView.layoutManager = layoutManager
        restaurantsRecyclerView.setHasFixedSize(true)
        restaurantAdapter =  RestaurantAdapter(view.context, restaurants)
        restaurantsRecyclerView.adapter = restaurantAdapter


        return  view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Call the new method within onViewCreated

        Thread(BackgroundFetchRestaurants()).start()
    }

    inner class BackgroundFetchRestaurants: Runnable {
        override  fun run() {

            fetchRestaurants()
        }
    }




    private fun fetchRestaurants() {
        //get public ip
        var ip: String? = null
        try {

            val url = URL("https://api.ipify.org")
            val connection = url.openConnection()
            connection.setRequestProperty("User-Agent", "Mozilla/5.0") // Set a User-Agent to avoid HTTP 403 Forbidden error
            val inputStream = connection.getInputStream()
            val s = java.util.Scanner(inputStream, "UTF-8").useDelimiter("\\A")
            ip = s.next()
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

        //get location data
        val loc_client = OkHttpClient()

        val loc_request = Request.Builder()
            .url("https://api.getgeoapi.com/v2/ip/check" +
                    "?api_key=${LOCATION_SEARCH_API_KEY}" +
                    "&format=json")
            .get()
            .build()

        val loc_response = loc_client.newCall(loc_request).execute()
        val jsonLoc = JSONObject(loc_response.body()!!.string())
        Log.v("API", jsonLoc.toString())

        //get area string
        val city = jsonLoc.getJSONObject("city").get("name").toString()
        val region = jsonLoc.getJSONObject("area").get("name").toString()
        val loc_string = city + ", " + region

        //get location id
        var client = OkHttpClient()

        var mediaType = MediaType.parse("application/x-www-form-urlencoded")
        var body = RequestBody.create(mediaType, "q=${URLEncoder.encode(loc_string).toString()}&language=en_US")
        var request = Request.Builder()
            .url("https://worldwide-restaurants.p.rapidapi.com/typeahead")
            .post(body)
            .addHeader("content-type", "application/x-www-form-urlencoded")
            .addHeader("X-RapidAPI-Key", "1de6516ce2mshdc6312d9d47f229p1036fejsn9fa66e182335")
            .addHeader("X-RapidAPI-Host", "worldwide-restaurants.p.rapidapi.com")
            .build()

        var response = client.newCall(request).execute()
        var locationBody = response.body()!!.string()
        val jsonLocation = JSONObject(locationBody).getJSONObject("results").getJSONArray("data")
        if (jsonLocation.length() > 0) {
            //use first location
            val location_id = JSONObject(jsonLocation[0].toString()).getJSONObject("result_object").get("location_id").toString()
            // Instantiate the RequestQueue.
            client = OkHttpClient()

            mediaType = MediaType.parse("application/x-www-form-urlencoded")
            body = RequestBody.create(
                mediaType,
                "language=en_US&location_id=${location_id}&currency=USD&offset=0"
            )
            request = Request.Builder()
                .url("https://worldwide-restaurants.p.rapidapi.com/search")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("X-RapidAPI-Key", "1de6516ce2mshdc6312d9d47f229p1036fejsn9fa66e182335")
                .addHeader("X-RapidAPI-Host", "worldwide-restaurants.p.rapidapi.com")
                .build()

            response = client.newCall(request).execute()
            val responseBody = response.body()!!.string()
            Log.v("API", responseBody)

            var jsonResponse = JSONObject(responseBody)
            var jsonRestaurants = jsonResponse.getJSONObject("results").getJSONArray("data")
            for (i in 0 until jsonRestaurants.length()) {
                val jsonRestaurant = JSONObject(jsonRestaurants[i].toString())
                val resName = jsonRestaurant.get("name")
                val resDesc = jsonRestaurant.get("description")
                val resLog = jsonRestaurant.get("longitude")
                val resLat = jsonRestaurant.get("latitude")
                val resRating = jsonRestaurant.get("rating")
                val resImg = jsonRestaurant.getJSONObject("photo")
                    .getJSONObject("images")
                    .getJSONObject("large")
                    .get("url")
                val resCuisines = ArrayList<String>()
                val jsonCuisines = jsonRestaurant.getJSONArray("cuisine")
                for (j in 0 until jsonCuisines.length()) {
                    resCuisines.add(JSONObject(jsonCuisines[j].toString()).get("name").toString())
                }
                restaurants.add(
                    Restaurant(
                        resName as String,
                        resImg as String,
                        resDesc as String,
                        resRating as String,
                        resLog as String,
                        resLat as String,
                        resCuisines
                    )
                )
            }
            val updateUI = Runnable {
                restaurantAdapter.notifyDataSetChanged()
            }
            Handler(Looper.getMainLooper()).post((updateUI))
        }

    }

    companion object {
        fun newInstance(): RestaurantList {
            return RestaurantList()
        }
    }
}