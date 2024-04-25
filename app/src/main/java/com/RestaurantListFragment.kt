package com

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tastebudzz.R
import com.facebook.shimmer.ShimmerFrameLayout
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.URL
import java.net.URLEncoder


private const val TAG = "RestaurantList"
private const val SEARCH_API_KEY = "1de6516ce2mshdc6312d9d47f229p1036fejsn9fa66e182335"
private const val RESTAURANT_SEARCH_URL = "1de6516ce2mshdc6312d9d47f229p1036fejsn9fa66e182335"
private const val LOCATION_SEARCH_API_KEY= "5ade6a67874d9716be26e95bee91bd09c52eaeed"
private const val FOOD_IMAGE_SEARCH_API_KEY= "2e97287ff29302d96ffd822e12248b27b86bfb14"// "76c387dfc382b4601d8deb6442847cfcf75e6f5d"// "28b1b1b079580cf999899e52c2aa7f7bb8016c39"

class RestaurantListFragment : Fragment() {


    private val restaurants = mutableListOf<Restaurant>()
    private lateinit var restaurantsRecyclerView: RecyclerView
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var shimmer: ShimmerFrameLayout
    private  lateinit var restaurantSearch: SearchView
    private lateinit var searchByImage: ImageView
    private var searchQueries = ArrayList<String>()
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_GALLERY_IMAGE = 2
    private var maxProb = 0

    private  var image_uri: Uri? = null
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
        restaurantSearch = view.findViewById(R.id.restaurantSearchBar)
        restaurantsRecyclerView.layoutManager = layoutManager
        restaurantsRecyclerView.setHasFixedSize(true)
        restaurantAdapter =  RestaurantAdapter(view.context, restaurants)
        restaurantsRecyclerView.adapter = restaurantAdapter
        searchByImage = view.findViewById(R.id.foodImageSearch)
        shimmer = view.findViewById(R.id.shimmer_view)
        shimmer.setVisibility(View.VISIBLE);
        shimmer.startShimmer();
        view.findViewById<SwipeRefreshLayout>(R.id.swiperefresh).setOnRefreshListener {
            restaurantSearch.isEnabled = false
            restaurants.clear()
            restaurantAdapter.notifyDataSetChanged()
            shimmer.setVisibility(View.VISIBLE);
            shimmer.startShimmer();
            Thread(BackgroundFetchRestaurants()).start()
            view.findViewById<SwipeRefreshLayout>(R.id.swiperefresh).isRefreshing = false

        }


        searchByImage.setOnClickListener {
            if (restaurants.size > 0) {
                Log.e("FOOD_IMAGE_API", "Dispatching image capture intent")

                dispatchTakePictureIntent()
            } else {
                Toast.makeText(context, "Restaurants still loading ...", Toast.LENGTH_SHORT).show()

            }
        }


        restaurantSearch.setOnClickListener {
            if (restaurants.size > 0) {
                restaurantSearch.isIconified = false
                Log.e("FOOD_IMAGE_API", "Restaurst resrtch acitvated")

            } else {
                restaurantSearch.isIconified = true

                Toast.makeText(context, "Restaurants still loading ...", Toast.LENGTH_SHORT).show()

            }
        }


        restaurantSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String): Boolean {
                if (restaurants.size > 0) {
                    filter(text, false)
                } else {
                    restaurantSearch.setQuery("", false)

                }
                return false
            }
        })
        return  view
    }


    private fun dispatchTakePictureIntent() {
        val options: Array<String> = arrayOf("Take Photo", "Choose from Gallery");
        val  builder = AlertDialog.Builder(context)
        builder.setTitle("Select Image Upload Method")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
                1 -> {
                    val pickPhoto = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE)
                }
            }

        }
        builder.create().show()
    }

    private fun fetchQueryFromImage(image: ByteArray) {
        Thread {
            var responseBody: String? = null
            try {
                val client = OkHttpClient()
                Log.e("FOOD_IMAGE_API", "image in bytes: ${image}")

                val analysisRequestBody: RequestBody =
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(
                            "image",
                            "food.jpeg",
                            RequestBody.create(MediaType.parse("image/jpg"), image)
                        )
                        .build()
                val food_info_request = Request.Builder()
                    .url("https://api.logmeal.com/v2/image/segmentation/complete/v1.0?language=eng")
                    .post(analysisRequestBody)
                    .addHeader("Authorization", "Bearer ${FOOD_IMAGE_SEARCH_API_KEY}")
                    .build()

                val response = client.newCall(food_info_request).execute()
                responseBody = response.body()!!.string()
                Log.e("FOOD_IMAGE_API", responseBody)

                if (!response.isSuccessful) {

                    Handler(Looper.getMainLooper()).post((updateUISearchFail))
                    return@Thread
                }


            } catch (e: Exception) {

                Handler(Looper.getMainLooper()).post((updateUISearchFail))
                return@Thread

            }

            if (responseBody == null) {
                Handler(Looper.getMainLooper()).post((updateUISearchFail))
                return@Thread
            } else {
                searchQueries.clear()
                val jsonFoodRecognition =
                    JSONObject(responseBody).getJSONArray("segmentation_results")

                for (i in 0 until jsonFoodRecognition.length()) {
                    val foodSegment =
                        JSONObject(jsonFoodRecognition[i].toString())
                    val recognitionResults = foodSegment.getJSONArray("recognition_results")
                    for (k in 0 until recognitionResults.length()) {

                        val recognizedFood = JSONObject(recognitionResults[k].toString())
                        Log.e("FOOD_IMAGE_API", "food segment: ${foodSegment.toString()}")

                        val foodName = recognizedFood.get("name").toString()
                        val prob =  recognizedFood.get("prob").toString().toDouble()
                        if (prob >= 0.15) {
                            Log.e("FOOD_IMAGE_API", "adding: ${foodName}")

                            addWords(foodName);

                            val foodFamily = recognizedFood.getJSONArray("foodFamily")
                            for (i in 0 until foodFamily.length()) {
                                Log.e("FOOD_IMAGE_API", "food family: ${foodFamily[i].toString()}")

                                val foodItem =
                                    JSONObject(foodFamily[i].toString()).get("name").toString()
                                Log.e("FOOD_IMAGE_API", "adding: ${foodName}")

                                addWords(foodName);
                            }
                        }
                    }
                }
            }
            Log.e("FOOD_IMAGE_API", "search filters: ${searchQueries.toString()}")
            val updateUI = Runnable {
                filter(searchQueries, true)
            }
            Handler(Looper.getMainLooper()).post(updateUI)
        }.start()

    }

    fun addWords(targetWord: String) {
        var words = targetWord.split(" ")
        for (word in words) {
            if (word.length > 3) {
                if (word[word.length -1 ] =='s') {
                    if (!searchQueries.contains(word.substring(0, word.length -1))) {
                        searchQueries.add(word.substring(0, word.length -1))

                    }
                } else {
                    if (!searchQueries.contains(word.substring(0, word.length))) {
                        searchQueries.add(word.substring(0, word.length))

                    }
                }
            }
        }
    }


    val updateUISearchFail = Runnable {
        if(shimmer.isShimmerVisible())
        {
            shimmer.stopShimmer();
            shimmer.setVisibility(View.GONE);
            restaurantsRecyclerView.visibility = View.VISIBLE
        }
        Toast.makeText(context, "Image search failed", Toast.LENGTH_SHORT).show()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Call the new method within onViewCreated
        Thread(BackgroundFetchRestaurants()).start()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // start shimmer
        shimmer.startShimmer();
        restaurantsRecyclerView.visibility = View.GONE
        shimmer.setVisibility(View.VISIBLE);

        if (resultCode == Activity.RESULT_OK) {
            Log.e("FOOD_IMAGE_API", "Image capture successful")

            //switching view with simmer
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                val photo = data!!.extras!!.get("data") as Bitmap?
                photo?.let {
                    val byteStream = ByteArrayOutputStream()
                    photo.compress(Bitmap.CompressFormat.JPEG, 60, byteStream)
                    val imageBytes = byteStream.toByteArray()
                    Log.e("FOOD_IMAGE_API", "Making API request for food data form image")
                    fetchQueryFromImage(imageBytes)
                }
            } else if (requestCode == REQUEST_GALLERY_IMAGE) {

                    val selectedImageUri = data?.data
                    selectedImageUri?.let {
                        /*val inputStream =
                            requireContext().contentResolver.openInputStream(selectedImageUri)
                        val imageBytes = inputStream?.readBytes()*/
                        val inputStream = requireContext().contentResolver.openInputStream(selectedImageUri)
                        inputStream?.let {
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            inputStream.close()

                            val outputStream = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
                            val byteArray = outputStream.toByteArray()
                            outputStream.close()
                            Log.e(
                                "FOOD_IMAGE_API",
                                "Making API request for food data form image with size ${byteArray.size}"
                            )
                            fetchQueryFromImage(byteArray)
                        }

                }
            }
        } else {
            if(shimmer.isShimmerVisible())
            {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                restaurantsRecyclerView.visibility= View.VISIBLE
            }
            Toast.makeText(context, "Image search failed", Toast.LENGTH_SHORT).show()
        }
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

        //val loc_string = "Newark, New Jersey"
        //get location id
        var client = OkHttpClient()

        var mediaType = MediaType.parse("application/x-www-form-urlencoded")
        var body = RequestBody.create(mediaType, "q=${URLEncoder.encode(loc_string).toString()}&language=en_US")
        var request = Request.Builder()
            .url("https://worldwide-restaurants.p.rapidapi.com/typeahead")
            .post(body)
            .addHeader("content-type", "application/x-www-form-urlencoded")
            .addHeader("X-RapidAPI-Key", SEARCH_API_KEY)
            .addHeader("X-RapidAPI-Host", "worldwide-restaurants.p.rapidapi.com")
            .build()
        Log.v("API", "location string: ${loc_string}")

        var response = client.newCall(request).execute()
        Log.v("API", "Passing restaurant data")
        try {
            val locationBody = response.body()!!.string()
            val jsonLocation = JSONObject(locationBody).getJSONObject("results").getJSONArray("data")
            if (jsonLocation.length() > 0) {
                //use first location
                val location_id = JSONObject(jsonLocation[0].toString()).getJSONObject("result_object")
                    .get("location_id").toString()
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
                    .addHeader("X-RapidAPI-Key", SEARCH_API_KEY)
                    .addHeader("X-RapidAPI-Host", "worldwide-restaurants.p.rapidapi.com")
                    .build()

                response = client.newCall(request).execute()
                val responseBody = response.body()!!.string()
                Log.v("API", responseBody)

                var jsonResponse = JSONObject(responseBody)
                var jsonRestaurants = jsonResponse.getJSONObject("results").getJSONArray("data")
                for (i in 0 until jsonRestaurants.length()) {
                    val jsonRestaurant = JSONObject(jsonRestaurants[i].toString())
                    val resId = jsonRestaurant.get("location_id").toString().toInt()
                    val resName = jsonRestaurant.get("name")
                    val resDesc = jsonRestaurant.get("description")
                    val resLog = jsonRestaurant.get("longitude")
                    val resLat = jsonRestaurant.get("latitude")
                    val resRating = jsonRestaurant.get("rating")
                    val resNumReviews = jsonRestaurant.get("num_reviews")
                    val resAddress = jsonRestaurant.get("address")
                    val resRanking = jsonRestaurant.get("ranking")

                    var resImg: String? = null

                    try {
                        resImg = jsonRestaurant.getJSONObject("photo")
                            .getJSONObject("images")
                            .getJSONObject("large")
                            .get("url") as String
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        resImg = ""
                    }

                    val resCuisines = ArrayList<String>()
                    val jsonCuisines = jsonRestaurant.getJSONArray("cuisine")
                    Log.v("API", jsonCuisines.toString())

                    for (j in 0 until jsonCuisines.length()) {

                        resCuisines.add(JSONObject(jsonCuisines[j].toString()).get("name").toString())
                    }
                    val restaurant = Restaurant(
                        resId,
                        resName as String,
                        resImg as String,
                        resDesc as String,
                        resRating as String,
                        resLog as String,
                        resLat as String,
                        resCuisines,
                        resRanking as String,
                        resNumReviews as String,
                        resAddress as String
                    )
                    Log.e("RESTAURANT", restaurant.toString())
                    restaurants.add(
                        restaurant
                    )
                }
                val updateUI = Runnable {
                    restaurantAdapter.notifyDataSetChanged()
                    if(shimmer.isShimmerVisible())
                    {
                        shimmer.stopShimmer();
                        shimmer.setVisibility(View.GONE);
                    }
                    restaurantSearch.isEnabled = true



                }
                Handler(Looper.getMainLooper()).post((updateUI))
            } else {
                Log.e("RESTAURANTS", "No near by restuaruants in your area")

                val updateUI = Runnable {
                    restaurantAdapter.notifyDataSetChanged()
                    if(shimmer.isShimmerVisible())
                    {
                        shimmer.stopShimmer();
                        shimmer.setVisibility(View.GONE);
                    }
                    restaurantSearch.isEnabled = true
                    Toast.makeText(context, "No restaurants found near you.", Toast.LENGTH_SHORT).show()


                }
                Handler(Looper.getMainLooper()).post((updateUI))
            }
        } catch (exception: JSONException) {
            Log.e("RESTAURANTS", exception.localizedMessage.toString())
            val updateUI = Runnable {
                restaurantAdapter.notifyDataSetChanged()
                if(shimmer.isShimmerVisible())
                {
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.GONE);
                }
                restaurantSearch.isEnabled = true



            }
            Handler(Looper.getMainLooper()).post((updateUI))
        }

    }

    companion object {
        fun newInstance(): RestaurantListFragment {
            return RestaurantListFragment()
        }
    }

    private fun filter(text: String, searchDescription: Boolean) {
        val filteredlist: ArrayList<Restaurant> = ArrayList()

        for (item in restaurants) {
            var addedRestaurant = false
            if (item.name != null && item.name!!.lowercase().contains(text.toLowerCase())) {

                filteredlist.add(item)
                addedRestaurant = true
            }

            if (searchDescription && !addedRestaurant) {
                if (item.description != null && item.description!!.lowercase().contains(text.toLowerCase())) {

                    filteredlist.add(item)
                    addedRestaurant = true
                }
            }
            if (!addedRestaurant) {
                val restaurantCuisines = item.cuisines;
                for (cuisine in restaurantCuisines) {
                    if (cuisine.lowercase().contains(text.toLowerCase())) {
                        filteredlist.add(item)
                        break
                    }
                }
            }
        }
        if (filteredlist.isEmpty()) {
            // Toast.makeText(context, "No restaurants with matching name or cuisine found..", Toast.LENGTH_SHORT).show()
        } else {
            restaurantAdapter.filterList(filteredlist)
        }
    }

    private fun filter(filters: ArrayList<String>, searchDescription: Boolean) {
        var iterCount = 0
        val filteredlist: ArrayList<Restaurant> = ArrayList()
        for (item in restaurants) {
            var addedRestaurant = false
            for (i in 0 until filters.size) {
                val text = filters[i]
                iterCount ++
                if (item.name != null && item.name!!.lowercase().contains(text.toLowerCase())) {
                    filteredlist.add(item)
                    addedRestaurant = true

                }

                if (!addedRestaurant) {
                    if (item.description != null && item.description!!.lowercase()
                            .contains(text.toLowerCase())
                    ) {

                        filteredlist.add(item)
                        addedRestaurant = true

                    }
                }

                if (!addedRestaurant) {
                    val restaurantCuisines = item.cuisines;
                    for (cuisine in restaurantCuisines) {
                        if (cuisine.lowercase().contains(text.toLowerCase())) {
                            filteredlist.add(item)
                            addedRestaurant = true
                        }
                    }
                }

                if (addedRestaurant) {
                    Log.e("FOOD_IMAGE_API", "add restaurant to ${item.name}")

                    break
                }

            }
        }
        Log.e("FOOD_IMAGE_API", "filter result: ${filteredlist.size} out of  ${restaurants.size}, took ${iterCount} iterations to process")

        if (filteredlist.isEmpty()) {
            Toast.makeText(context, "No restaurants with matching the image was found.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "List updated with ${filteredlist.size} matching restaurants.", Toast.LENGTH_SHORT).show()

            restaurantAdapter.filterList(filteredlist)
        }
        if(shimmer.isShimmerVisible())
        {
            restaurantSearch.isEnabled = true

            shimmer.stopShimmer();
            shimmer.setVisibility(View.GONE);
            restaurantsRecyclerView.visibility = View.VISIBLE

        }
    }
}