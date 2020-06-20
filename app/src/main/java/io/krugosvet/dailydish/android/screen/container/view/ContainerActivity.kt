package io.krugosvet.dailydish.android.screen.container.view

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import io.krugosvet.bindingcomponent.BindingComponent
import io.krugosvet.dailydish.android.BR
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.extension.setVisibility
import io.krugosvet.dailydish.android.architecture.injection.activityInject
import io.krugosvet.dailydish.android.architecture.view.BaseActivity
import io.krugosvet.dailydish.android.databinding.ActivityContainerBinding
import io.krugosvet.dailydish.android.screen.container.viewmodel.ContainerViewModel
import io.krugosvet.dailydish.android.screen.mealList.view.MealListFragmentDirections
import io.krugosvet.dailydish.android.service.KeyboardService

class ContainerActivity :
  BaseActivity<ActivityContainerBinding, ContainerViewModel>() {

  override val viewModel: ContainerViewModel by viewModels()
  override val bindingComponent = BindingComponent(R.layout.activity_container, this, BR.viewModel)

  override val parentContext: Context = this

  private val keyboardService: KeyboardService by activityInject()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    viewModel.navigationEvent.observe(this, Observer {
      when (it) {
        ContainerViewModel.Event.ShowAddMeal -> showAddMeal()
      }
    })
  }

  override fun onBind() {
    super.onBind()

    setupActionBarWithNavController(navController, bindingComponent.binding.drawerLayout)
    NavigationUI.setupWithNavController(bindingComponent.binding.navView, navController)

    navController.addOnDestinationChangedListener { _, destination, _ ->
      bindingComponent.binding.floatingButton.setVisibility(destination.id == R.id.mealListFragment)

      keyboardService.hideKeyboard()
    }
  }

  override fun onSupportNavigateUp() =
    NavigationUI.navigateUp(navController, bindingComponent.binding.drawerLayout)

  private fun showAddMeal() {
    navController.navigate(MealListFragmentDirections.actionMealListFragmentToDialogAddMeal())
  }
}
