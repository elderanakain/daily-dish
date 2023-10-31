package io.krugosvet.dailydish.android.ui.addMeal

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.BaseFragment
import io.krugosvet.dailydish.android.architecture.hideKeyboard
import io.krugosvet.dailydish.android.databinding.DialogAddMealBinding
import io.krugosvet.dailydish.android.ui.addMeal.AddMealViewModel.Event
import io.krugosvet.dailydish.common.core.currentDate
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.LocalDate

@AndroidEntryPoint
class AddMealFragment :
    BaseFragment<DialogAddMealBinding, AddMealViewModel>(R.layout.dialog_add_meal),
    DatePickerDialog.OnDateSetListener {

    override val viewModel: AddMealViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addMealButton.setOnClickListener {
            activity?.hideKeyboard()
            viewModel.onAddMeal()
        }

        viewModel.navigationEvent
            .flowWithLifecycle(lifecycle)
            .onEach { event ->
                when (event) {
                    is Event.Close -> navController.popBackStack()
                    is Event.ShowDatePicker -> showDatePicker()
                }
            }
            .launchInCatching()

        val now = currentDate

        onDateSet(null, now.year, now.monthNumber - 1, now.dayOfMonth)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val newDate = LocalDate(year, month + 1, dayOfMonth)

        viewModel.onDateChange(newDate)
    }

    private fun showDatePicker() {
        val now = currentDate

        DatePickerDialog(requireContext(), this, now.year, now.monthNumber - 1, now.dayOfMonth)
            .show()
    }
}
