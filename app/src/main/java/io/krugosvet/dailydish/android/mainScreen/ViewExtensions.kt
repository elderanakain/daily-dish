package io.krugosvet.dailydish.android.mainScreen

import android.view.View
import androidx.databinding.BindingAdapter

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
