package io.krugosvet.dailydish.android.utils

import android.content.*
import android.view.View.*
import android.view.inputmethod.*
import android.view.inputmethod.InputMethodManager.*
import android.widget.*

fun showKeyboard(editText: EditText) {
  editText.onFocusChangeListener = OnFocusChangeListener { _, _ ->
    editText.post {
      getInputMethodManager(editText.context).showSoftInput(editText, SHOW_IMPLICIT)
    }
  }
  editText.requestFocus()
}

private fun getInputMethodManager(context: Context) =
  context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
