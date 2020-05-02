package io.krugosvet.dailydish.android.architecture.ui.binding

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("android:onClick")
fun View.setOnClickListener(listener: (() -> Unit)?) {
  listener ?: return

  setOnClickListener {
    listener.invoke()
  }
}
