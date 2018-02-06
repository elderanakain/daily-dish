package io.krugosvet.dailydish.android.utils.errorBox

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.TextView
import io.krugosvet.dailydish.android.utils.SimpleTextWatcher

class ErrorBoxImpl @JvmOverloads constructor(context: Context, attr: AttributeSet? = null, defStyleAttr: Int = 0)
    : TextView(context, attr, defStyleAttr) {

    private val textWatcher = ErrorBoxTextWatcher()

    init {
        setTextColor(Color.RED)
        visibility = View.INVISIBLE
    }

    fun subscribeToEditText(editText: EditText, errorText: String) {
        text = errorText
        editText.addTextChangedListener(textWatcher)
        textWatcher.onTextChanged(editText.text, 0, 0, editText.text.count())
    }

    inner class ErrorBoxTextWatcher : SimpleTextWatcher {

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            visibility = if (count == 0) View.VISIBLE else View.INVISIBLE
        }
    }
}
