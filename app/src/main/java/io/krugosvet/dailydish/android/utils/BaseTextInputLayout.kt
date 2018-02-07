package io.krugosvet.dailydish.android.utils

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.text.TextWatcher
import android.util.AttributeSet

class BaseTextInputLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : TextInputLayout(context, attrs, defStyleAttr) {

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
