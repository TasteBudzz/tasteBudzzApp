package com

import java.io.Serializable

data class Recipe (

    var id: String,

    var userId: String,

    var recipeId: String,

    var name: String,

    var recipeImageURL: String?,

    var nutritionInformation: ArrayList<String>,

    // TODO: crete new type for ingredietnes (amount?, weight?, and so on as givin in API)
    // var ingredients: ArrayList<>,

    // TODO: crete new type for instruction (time? as givin in API)
    // var instructions: ArrayList<E>,

): Serializable