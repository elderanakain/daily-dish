package io.krugosvet.dailydish.android.utils.errorBox

import android.widget.EditText

interface ErrorBox {
    fun subscribeToEditText(editText: EditText, errorText: String)
}