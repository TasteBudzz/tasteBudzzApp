package com

import java.io.Serializable

data class Recipe (
    var id: String,
    var userId: String,
    var recipeId: String,
    var name: String,
    var recipeImageURL: String?,
    var nutritionInformation: ArrayList<String>,
    var ingredients: ArrayList<String>,
    var instructions: String,
    var restaurantName: String
) : Serializable {
    // No-argument constructor
    constructor() : this("", "", "", "", null, ArrayList(), ArrayList(), "", "")
}
