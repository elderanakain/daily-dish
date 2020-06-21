package io.krugosvet.dailydish.android.screen.mealList.view

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
import io.krugosvet.dailydish.android.screen.mealList.viewmodel.MealListViewModel
import io.krugosvet.dailydish.android.service.ImageService
import org.koin.androidx.viewmodel.ext.android.viewModel

class MealListFragment :
  BaseFragment<FragmentMealListBinding, MealListViewModel>() {

  override val parentContext: Context
    get() = requireContext()

  override val viewModel by viewModel<MealListViewModel>()
  override val bindingComponent = BindingComponent(R.layout.fragment_meal_list, this, BR.viewModel)

  private val imageService: ImageService by activityInject()

  private val adapter: MealListAdapter by lazy { MealListAdapter(this) }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    bindingComponent.binding.mealList.adapter = adapter

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
    imageService.showImagePicker(event.meal.imageUri.isEmpty())
      .subscribeOnIoThread(onSuccess = { image -> viewModel.changeImage(event.meal, image) })
      .store()
  }
}
