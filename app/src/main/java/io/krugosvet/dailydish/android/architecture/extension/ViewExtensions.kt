package io.krugosvet.dailydish.android.architecture.extension

import android.net.Uri
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.krugosvet.dailydish.android.R

typealias OnClick = () -> Unit

@BindingAdapter("android:visibility")
fun View.setVisibility(isVisible: Boolean) {
  visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("srcUri")
fun ImageView.srcUri(image: Uri?) {
  Glide.with(this)
    .applyDefaultRequestOptions(emptyMainImage)
    .load(if (image.isEmpty) null else image)
    .into(this)
}

val Uri?.isEmpty: Boolean
  get() = this == Uri.EMPTY

private val emptyMainImage by lazy {
  RequestOptions()
    .placeholder(R.drawable.meal_empty_image)
    .centerCrop()
}

@BindingAdapter("error")
fun EditText.setError(error: Int?) {
  val errorText = if (error == null) null else resources.getString(error)

  setError(errorText)
}
