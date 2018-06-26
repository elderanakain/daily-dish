package io.krugosvet.dailydish.android.network.json

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.util.*

data class UpdateImageMeal(@SerializedName("id") var id: Int,
                           @JsonAdapter(MainImageTypeAdapter::class)
                           @SerializedName("main_image") var mainImage: String,
                           @SerializedName("user_id") var userId: String)

data class UpdateDateMeal(@SerializedName("id") var id: Int,
                          @SerializedName("date") var date: Date,
                          @SerializedName("user_id") var userId: String)
