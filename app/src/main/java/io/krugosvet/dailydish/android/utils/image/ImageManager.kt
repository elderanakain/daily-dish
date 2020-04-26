package io.krugosvet.dailydish.android.utils.image

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
