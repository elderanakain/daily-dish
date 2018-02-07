package io.krugosvet.dailydish.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.krugosvet.dailydish.android.utils.BaseDialogFragment
import io.krugosvet.dailydish.android.utils.BaseTextInputLayout
import io.krugosvet.dailydish.android.utils.SimpleTextWatcher
import io.krugosvet.dailydish.android.utils.showKeyboard
import kotlinx.android.synthetic.main.dialog_add_meal.*
import java.text.DateFormat
import java.util.*

class DialogAddMeal : BaseDialogFragment(), DatePickerDialog.OnDateSetListener {

    interface DialogAddMealListener {
        fun onAddButtonClick(mealTitle: String, mealDescription: String)
    }

    private val forms = mutableListOf<BaseTextInputLayout>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = inflater.inflate(R.layout.dialog_add_meal, container)

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

        date.editText?.setOnClickListener {
            forms.forEach { it.clearFocus() }
            addMealButton.requestFocus()
            DatePickerDialog.newInstance(this, Calendar.getInstance()).show(fragmentManager, "")
        }
    }

    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(getDimension(R.dimen.dialog_add_meal_width).toInt(), WRAP_CONTENT)
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        date.editText?.setText(DateFormat.getDateInstance(DateFormat.LONG).format(
                Calendar.getInstance().also {
                    it.set(year, monthOfYear, dayOfMonth)
                }.time))
    }

    private fun handleForms() {
        forms.addAll(listOf(title, description, date))
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
