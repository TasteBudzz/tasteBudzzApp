package com
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Restaurant (

    var name: String?,

    var restaurantImageURL: String?,

    var description: String?,

    var rating: String?,

    var latitude: String?,

    var longitude: String?,

    var cuisines: ArrayList<String>
)