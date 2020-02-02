package io.krugosvet.dailydish.android.network.json

import java.util.*

data class UpdateImageMeal(
  var id: Int,
  var mainImage: String,
  var userId: String = ""
)

data class UpdateDateMeal(
  var id: Int,
  var date: Date,
  var userId: String = ""
)
