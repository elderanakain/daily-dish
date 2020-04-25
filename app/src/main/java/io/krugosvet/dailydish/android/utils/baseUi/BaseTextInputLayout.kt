package io.krugosvet.dailydish.android.utils.baseUi

import android.content.*
import android.text.*
import android.util.*
import com.google.android.material.textfield.*

class BaseTextInputLayout @JvmOverloads constructor(
    context: Context, attrs:
    AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    TextInputLayout(context, attrs, defStyleAttr) {

  private var textWatcher: TextWatcher? = null

  fun getEditTextInput() = editText?.text.toString()

  fun triggerTextWatcher() {
    val text = getEditTextInput()
    textWatcher?.onTextChanged(text, 0, text.length, 0)
  }

  fun addTextChangedListener(watcher: TextWatcher) {
    editText?.addTextChangedListener(watcher)
    textWatcher = watcher
  }
}
