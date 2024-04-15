package com

import java.io.Serializable

data class FoodItem(

    val name: String,

    val price: String,

    val imageResId: Int

): Serializable

