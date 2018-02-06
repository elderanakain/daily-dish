package io.krugosvet.dailydish.android.utils

import android.content.Context
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.EditText

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
