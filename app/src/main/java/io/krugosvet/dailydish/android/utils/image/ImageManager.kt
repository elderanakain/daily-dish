package io.krugosvet.dailydish.android.utils.image

import android.widget.*
import androidx.annotation.*
import com.bumptech.glide.*
import com.bumptech.glide.request.*
import io.krugosvet.dailydish.android.R

fun loadMealMainImage(imageView: ImageView, image: String?) {
  Glide.with(imageView)
    .applyDefaultRequestOptions(getRequestOptions())
    .load(image)
    .into(imageView)
}

fun loadMealMainImage(imageView: ImageView, @DrawableRes image: Int) {
  Glide.with(imageView)
    .applyDefaultRequestOptions(getRequestOptions())
    .load(image)
    .into(imageView)
}

private fun getRequestOptions() =
    RequestOptions()
  .placeholder(R.drawable.meal_empty_main_image)
  .centerCrop()
