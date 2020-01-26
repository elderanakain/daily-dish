package io.krugosvet.dailydish.android.utils

import android.text.*

interface SimpleTextWatcher: TextWatcher {
  override fun afterTextChanged(s: Editable?) {}

  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
}
