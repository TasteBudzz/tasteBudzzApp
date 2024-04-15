package com

import java.io.Serializable
import java.sql.Timestamp

data class Review (

    var userId: String?,

    var restaurantId: String?,

    var rating: Double?,

    var comment: String?,

    var reviewerName: String?,

    var timestamp: Long

    ): Serializable