package com

import java.io.Serializable

data class Recipe (

    var id: String,

    var name: String,

    var recipeImageURL: String?,

    var instructions: String?,

    var nutritionInformation: ArrayList<String>,
// TODO add instructions and ingridents data types
): Serializable