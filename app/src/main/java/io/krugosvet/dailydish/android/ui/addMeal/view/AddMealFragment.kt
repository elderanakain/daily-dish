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
import io.krugosvet.dailydish.core.service.DateService
import io.krugosvet.dailydish.core.service.day
import io.krugosvet.dailydish.core.service.month
import io.krugosvet.dailydish.core.service.year
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddMealFragment :
  BaseFragment<DialogAddMealBinding, AddMealViewModel>(),
  DatePickerDialog.OnDateSetListener {

  override val viewModel: AddMealViewModel by viewModel()
  override val bindingComponent = BindingComponent(R.layout.dialog_add_meal, this, BR.viewModel)

  private val dateService: DateService by inject()

  private val imagePickerService: ImagePickerService by activityInject()

  override val parentContext: Context
    get() = requireContext()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.navigationEvent.observe {
      when (val event = it) {
        is Event.Close -> close()
        is Event.ShowImagePicker -> showImagePicker(event)
        is Event.ShowDatePicker -> showDatePicker()
      }
    }

    with(dateService.currentDate) {
      onDateSet(null, year, month, day)
    }
  }

  override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
    val newDate = dateService.format(year, month + 1, dayOfMonth)

    viewModel.onDateChange(newDate)
  }

  private fun showImagePicker(event: Event.ShowImagePicker) = launch {
    val image = imagePickerService.showImagePicker(event.isImageEmpty)

    viewModel.onImageChange(image)
  }

  private fun showDatePicker() {
    with(dateService.currentDate) {
      DatePickerDialog(requireContext(), this@AddMealFragment, year, month, day)
        .show()
    }
  }

  private fun close() {
    activity?.hideKeyboard()
    navController.popBackStack()
  }
}
