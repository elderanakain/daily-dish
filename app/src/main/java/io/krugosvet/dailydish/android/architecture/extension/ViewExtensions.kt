package io.krugosvet.dailydish.android.architecture.extension

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.krugosvet.dailydish.android.R

typealias OnClick = () -> Unit

@BindingAdapter("android:onClick")
fun View.setOnClickListener(listener: (() -> Unit)?) {
  listener ?: return

  setOnClickListener {
    listener.invoke()
  }
}

@BindingAdapter("android:visibility")
fun View.setVisibility(isVisible: Boolean) {
  visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("srcUri")
fun ImageView.srcUri(image: String?) {
  Glide.with(this)
    .applyDefaultRequestOptions(emptyMainImage)
    .load(image)
    .into(this)
}

private val emptyMainImage by lazy {
  RequestOptions()
    .placeholder(R.drawable.meal_empty_image)
    .centerCrop()
}
