package com
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User (

    var id: String,

    var firstName: String?,

    var lastName: String?,

    var email: String
): Serializable