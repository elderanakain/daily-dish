package io.krugosvet.dailydish.android.ui.mealList.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import io.krugosvet.dailydish.android.BR
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.aspect.BindingComponent
import io.krugosvet.dailydish.android.architecture.injection.activityInject
import io.krugosvet.dailydish.android.architecture.view.BaseFragment
import io.krugosvet.dailydish.android.databinding.FragmentMealListBinding
import io.krugosvet.dailydish.android.repository.meal.MealImage
import io.krugosvet.dailydish.android.service.ImagePickerService
import io.krugosvet.dailydish.android.ui.mealList.viewmodel.MealListViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class MealListFragment :
  BaseFragment<FragmentMealListBinding, MealListViewModel>() {

  override val parentContext: Context
    get() = requireContext()

  override val viewModel by stateViewModel<MealListViewModel>()
  override val bindingComponent = BindingComponent(R.layout.fragment_meal_list, this, BR.viewModel)

  private val imagePickerService: ImagePickerService by activityInject()
  private val mealListDecorator: MealListDecorator by inject()

  private val adapter: MealListAdapter by lazy { MealListAdapter() }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.mealList.apply {
      adapter = this@MealListFragment.adapter

      if (itemDecorationCount == 0) {
        addItemDecoration(mealListDecorator)
      }
    }

    viewModel.mealList.observe(viewLifecycleOwner, { mealList ->
      adapter.submitData(lifecycle, mealList)
    })

    viewModel.navigationEvent.observe(viewLifecycleOwner, { event ->
      when (event) {
        is MealListViewModel.Event.ShowImagePicker -> showImagePicker(event)
      }
    })

    adapter.loadStateFlow
      .map { it.refresh }
      .onEach { state ->
        viewModel.onPagingStateChange(state)
      }
      .launchIn(lifecycleScope)
  }

  private fun showImagePicker(event: MealListViewModel.Event.ShowImagePicker) {
    imagePickerService.showImagePicker(event.meal.image.isEmpty)
      .onEach { image ->
        viewModel.changeImage(event.meal, MealImage(image))
      }
      .launchIn(viewLifecycleOwner.lifecycle.coroutineScope)
  }
}

