package io.krugosvet.dailydish.android.architecture.extension

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
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

@BindingAdapter("android:src")
fun ImageView.src(image: Uri?) {
    Glide.with(this)
        .run {
            when {
                image.isEmpty -> load(emptyMainImage)
                else -> applyDefaultRequestOptions(emptyMainImage)
                    .load(image)
            }
        }
        .into(this)
}

@BindingAdapter("android:src")
fun ImageView.src(image: ByteArray?) {
    Glide.with(this)
        .applyDefaultRequestOptions(emptyMainImage)
        .load(image)
        .into(this)
}

private val Uri?.isEmpty: Boolean
    get() = this == null || this == Uri.EMPTY

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

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    view?.let { v ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}
