package io.krugosvet.dailydish.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import io.krugosvet.dailydish.android.utils.BaseDialogFragment
import io.krugosvet.dailydish.android.utils.showKeyboard
import kotlinx.android.synthetic.main.dialog_add_meal.*

class DialogAddMeal : BaseDialogFragment() {

    interface DialogAddMealListener {
        fun onAddButtonClick(mealTitle: String, mealDescription: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = inflater.inflate(R.layout.dialog_add_meal, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showKeyboard(mealTitle.editText!!)

        addMealButton.setOnClickListener {
            val mealTitleText = mealTitle.editText?.text.toString()
            val mealDescriptionText = mealDescription.editText?.text.toString()

            when {
                mealTitleText.isEmpty() -> mealTitle.error = getString(R.string.dialog_add_meal_empty_title_error)
                mealDescriptionText.isEmpty() -> mealDescription.error = getString(R.string.dialog_add_meal_empty_description_error)
                mealTitleText.isEmpty() || mealDescriptionText.isEmpty() -> return@setOnClickListener
            }
            (activity as DialogAddMealListener).onAddButtonClick(mealTitleText, mealDescriptionText)
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(getDimension(R.dimen.dialog_add_meal_width).toInt(), LayoutParams.WRAP_CONTENT)
    }
}
