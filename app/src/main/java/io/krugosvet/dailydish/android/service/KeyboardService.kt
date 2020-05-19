package io.krugosvet.dailydish.android.service

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import io.krugosvet.dailydish.android.architecture.view.GenericBaseActivity

class KeyboardService(
  private val activity: GenericBaseActivity
) {

  private val inputMethodManager
    get() = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

  fun showKeyboard(view: View) {
    if (view.requestFocus()) {
      inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
  }

  fun hideKeyboard() =
    inputMethodManager.hideSoftInputFromWindow(
      (activity.currentFocus ?: View(activity)).windowToken, 0
    )
}
