package io.krugosvet.dailydish.android.ui.mealList.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import io.krugosvet.dailydish.android.BR
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.aspect.BindingComponent
import io.krugosvet.dailydish.android.architecture.extension.subscribeOnIoThread
import io.krugosvet.dailydish.android.architecture.injection.activityInject
import io.krugosvet.dailydish.android.architecture.view.BaseFragment
import io.krugosvet.dailydish.android.databinding.FragmentMealListBinding
import io.krugosvet.dailydish.android.repository.meal.MealImage
import io.krugosvet.dailydish.android.service.ImagePickerService
import io.krugosvet.dailydish.android.ui.mealList.viewmodel.MealListViewModel
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

  private val adapter: MealListAdapter by lazy { MealListAdapter(this) }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.mealList.apply {
      adapter = this@MealListFragment.adapter

      if (itemDecorationCount == 0) {
        addItemDecoration(mealListDecorator)
      }
    }

    viewModel.mealList.observe(viewLifecycleOwner, Observer { mealList ->
      adapter.submitList(mealList)
    })

    viewModel.navigationEvent.observe(viewLifecycleOwner, Observer { event ->
      when (event) {
        is MealListViewModel.Event.ShowImagePicker -> showImagePicker(event)
      }
    })
  }

  private fun showImagePicker(event: MealListViewModel.Event.ShowImagePicker) {
    imagePickerService.showImagePicker(event.meal.image.isEmpty)
      .subscribeOnIoThread(onSuccess = { image ->
        viewModel.changeImage(event.meal, MealImage(image))
      })
      .store()
  }
}

