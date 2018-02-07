package io.krugosvet.dailydish.android.utils

import android.text.Editable
import android.text.TextWatcher

interface SimpleTextWatcher: TextWatcher {
    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
}
