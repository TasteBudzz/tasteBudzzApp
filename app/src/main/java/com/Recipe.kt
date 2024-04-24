package com

import java.io.Serializable

data class Recipe (

    var id: String,

    var userId: String,

    var recipeId: String,

    var name: String,

    var recipeImageURL: String?,

    var nutritionInformation: ArrayList<String>,

    // TODO: create new type for ingredietnes (amount?, weight?, and so on as given in API)
    var ingredients: ArrayList<String>,

    // TODO: create new type for instruction (time? as given in API)
    var instructions: String,

    var restaurantName: String,


    ) : Serializable