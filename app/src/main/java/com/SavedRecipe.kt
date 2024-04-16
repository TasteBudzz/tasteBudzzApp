package com

data class SavedRecipe(
    val recipeName: String,
    val restaurantName: String,
    val ingredients: List<String>,
    val instructions: String,
    val nutrition: String
)
