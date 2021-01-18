package io.krugosvet.dailydish.android.ui.addMeal.view

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import io.krugosvet.dailydish.android.BR
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.aspect.BindingComponent
import io.krugosvet.dailydish.android.architecture.extension.hideKeyboard
import io.krugosvet.dailydish.android.architecture.injection.activityInject
import io.krugosvet.dailydish.android.architecture.view.BaseFragment
import io.krugosvet.dailydish.android.databinding.DialogAddMealBinding
import io.krugosvet.dailydish.android.service.ImagePickerService
import io.krugosvet.dailydish.android.ui.addMeal.viewmodel.AddMealViewModel
import io.krugosvet.dailydish.android.ui.addMeal.viewmodel.AddMealViewModel.Event
import io.krugosvet.dailydish.common.core.currentDate
import kotlinx.datetime.LocalDate
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddMealFragment :
  BaseFragment<DialogAddMealBinding, AddMealViewModel>(),
  DatePickerDialog.OnDateSetListener {

  override val viewModel: AddMealViewModel by stateViewModel()
  override val bindingComponent = BindingComponent(R.layout.dialog_add_meal, this, BR.viewModel)

  private val imagePickerService: ImagePickerService by activityInject()

  override val parentContext: Context
    get() = requireContext()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.addMealButton.setOnClickListener {
      activity?.hideKeyboard()
      viewModel.onAddMeal()
    }

    viewModel.navigationEvent.observe {
      when (val event = it) {
        is Event.Close -> navController.popBackStack()
        is Event.ShowImagePicker -> showImagePicker(event)
        is Event.ShowDatePicker -> showDatePicker()
      }
    }

    with(currentDate) {
      onDateSet(null, year, monthNumber, dayOfMonth)
    }
  }

  override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
    val newDate = LocalDate(year, month, dayOfMonth)

    viewModel.onDateChange(newDate)
  }

  private fun showImagePicker(event: Event.ShowImagePicker) = launch {
    val image = imagePickerService.showImagePicker(event.isImageEmpty)

    viewModel.onImageChange(image)
  }

  private fun showDatePicker() {
    with(currentDate) {
      DatePickerDialog(requireContext(), this@AddMealFragment, year, monthNumber, dayOfMonth)
        .show()
    }
  }
}
