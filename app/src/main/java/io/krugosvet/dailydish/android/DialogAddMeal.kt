package io.krugosvet.dailydish.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import io.krugosvet.dailydish.android.utils.BaseDialogFragment
import io.krugosvet.dailydish.android.utils.showKeyboard
import kotlinx.android.synthetic.main.dialog_add_meal.*
import kotterknife.bindView


class DialogAddMeal : BaseDialogFragment() {

    private val addMealButton by bindView<TextView>(R.id.add_meal_button)

    interface DialogAddMealListener {
        fun onAddButtonClick(mealTitle: String, mealDescription: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.dialog_add_meal, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showKeyboard(activity!!)

        addMealButton.findViewById<TextView>(R.id.add_meal_button).setOnClickListener {
            val mealTitle = mealTitle.text.toString()

            if (mealTitle.isEmpty()) {
                error.text = getString(R.string.dialog_add_meal_empty_title_toast)
            } else {
                (activity as DialogAddMealListener).onAddButtonClick(mealTitle, mealDescription.text.toString())
                dismiss()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }
}