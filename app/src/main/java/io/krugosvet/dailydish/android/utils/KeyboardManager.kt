package io.krugosvet.dailydish.android.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun showKeyboard(editText: EditText) {
    editText.onFocusChangeListener = OnFocusChangeListener { _, _ ->
        editText.post {
            getInputMethodManager(editText.context).showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }
    editText.requestFocus()
}

fun hideKeyboard(activity: Activity?) {
    val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = activity.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun getInputMethodManager(context: Context) =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager