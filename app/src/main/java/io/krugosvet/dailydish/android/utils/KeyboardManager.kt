package io.krugosvet.dailydish.android.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun showKeyboard(activity: Activity) {
    getInputMethodManager(activity).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun hideKeyboard(activity: Activity) {
    getInputMethodManager(activity).hideSoftInputFromWindow(
            (if (activity.currentFocus == null) View(activity)
            else activity.currentFocus).windowToken, 0)
}

private fun getInputMethodManager(activity: Activity) =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager