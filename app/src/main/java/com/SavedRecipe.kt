package com

import java.io.Serializable

data class SavedRecipe(
    val recipeName: String,
    val restaurantName: String,
    val ingredients: List<String>,
    val instructions: String,
    val nutrition: String
) : Serializable
