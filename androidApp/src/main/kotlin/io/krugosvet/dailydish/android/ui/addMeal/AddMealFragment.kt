package io.krugosvet.dailydish.android.ui.addMeal

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.lifecycle.flowWithLifecycle
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.aspect.BindingComponent
import io.krugosvet.dailydish.android.architecture.extension.hideKeyboard
import io.krugosvet.dailydish.android.architecture.view.BaseFragment
import io.krugosvet.dailydish.android.databinding.DialogAddMealBinding
import io.krugosvet.dailydish.android.ui.addMeal.AddMealViewModel.Event
import io.krugosvet.dailydish.common.core.currentDate
import io.krugosvet.dailydish.common.dto.NewImage
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.LocalDate
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddMealFragment :
    BaseFragment<DialogAddMealBinding, AddMealViewModel>(),
    DatePickerDialog.OnDateSetListener {

    override val viewModel: AddMealViewModel by viewModel()
    override val bindingComponent = BindingComponent(R.layout.dialog_add_meal, this)

    override val parentContext: Context
        get() = requireContext()

    private val pickMedia = registerForActivityResult(PickVisualMedia()) { image ->
        image ?: return@registerForActivityResult

        val contentResolver = requireContext().contentResolver

        val bytes = contentResolver.openInputStream(image)
            ?.use { it.buffered().readBytes() }
            ?: return@registerForActivityResult

        viewModel.onImageChange(
            NewImage(
                bytes,
                extension = contentResolver.getType(image) ?: ""
            )
        )
    }

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
                    is Event.ShowImagePicker -> showImagePicker()
                    is Event.ShowDatePicker -> showDatePicker()
                }
            }
            .launchInCatching()

        with(currentDate) {
            onDateSet(null, year, monthNumber, dayOfMonth)
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val newDate = LocalDate(year, month, dayOfMonth)

        viewModel.onDateChange(newDate)
    }

    private fun showImagePicker() = launch {
        pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }

    private fun showDatePicker() {
        with(currentDate) {
            DatePickerDialog(requireContext(), this@AddMealFragment, year, monthNumber, dayOfMonth)
                .show()
        }
    }
}
