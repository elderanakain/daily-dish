package io.krugosvet.dailydish.android.ui.container.view

import android.content.Context
import android.os.Bundle
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import io.krugosvet.dailydish.android.BR
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.aspect.BindingComponent
import io.krugosvet.dailydish.android.architecture.extension.setVisibility
import io.krugosvet.dailydish.android.architecture.view.BaseActivity
import io.krugosvet.dailydish.android.databinding.ActivityContainerBinding
import io.krugosvet.dailydish.android.ui.container.viewmodel.ContainerViewModel
import io.krugosvet.dailydish.android.ui.mealList.view.MealListFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContainerActivity :
  BaseActivity<ActivityContainerBinding, ContainerViewModel>() {

  override val viewModel: ContainerViewModel by viewModel()
  override val bindingComponent = BindingComponent(R.layout.activity_container, this, BR.viewModel)

  override val parentContext: Context = this

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    viewModel.navigationEvent.observe(this, {
      when (it) {
        ContainerViewModel.Event.ShowAddMeal -> showAddMeal()
      }
    })
  }

  override fun onBind() {
    super.onBind()

    setupActionBarWithNavController(navController, binding.drawerLayout)
    NavigationUI.setupWithNavController(binding.navView, navController)

    navController.addOnDestinationChangedListener { _, destination, _ ->
      binding.floatingButton.setVisibility(destination.id == R.id.mealListFragment)
    }
  }

  override fun onSupportNavigateUp() =
    NavigationUI.navigateUp(navController, binding.drawerLayout)

  private fun showAddMeal() {
    navController.navigate(MealListFragmentDirections.actionMealListFragmentToDialogAddMeal())
  }
}
