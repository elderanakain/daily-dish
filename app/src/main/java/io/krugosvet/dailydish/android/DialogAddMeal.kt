package io.krugosvet.dailydish.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import io.krugosvet.dailydish.android.utils.BaseDialogFragment
import io.krugosvet.dailydish.android.utils.BaseTextInputLayout
import io.krugosvet.dailydish.android.utils.SimpleTextWatcher
import io.krugosvet.dailydish.android.utils.showKeyboard
import kotlinx.android.synthetic.main.dialog_add_meal.*

class DialogAddMeal : BaseDialogFragment() {

    interface DialogAddMealListener {
        fun onAddButtonClick(mealTitle: String, mealDescription: String)
    }

    private val forms = mutableListOf<BaseTextInputLayout>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.dialog_add_meal, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showKeyboard(title.editText!!)
        handleForms()

        addMealButton.setOnClickListener {
            if (areFormsValid()) {
                (activity as DialogAddMealListener)
                        .onAddButtonClick(title.getEditTextInput(), description.getEditTextInput())
                dismiss()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(getDimension(R.dimen.dialog_add_meal_width).toInt(), WRAP_CONTENT)
    }

    private fun handleForms() {
        forms.addAll(listOf(title, description))
        forms.forEach {
            it.addTextChangedListener(object : SimpleTextWatcher {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.isNullOrBlank()) it.error = getString(R.string.dialog_add_meal_empty_form_error, it.tag)
                    else it.isErrorEnabled = false
                }
            })
        }
    }

    private fun areFormsValid(): Boolean {
        var isValid = true
        forms.forEach {
            it.triggerTextWatcher()
            if (it.isErrorEnabled && isValid) isValid = false
        }
        return isValid
    }
}
