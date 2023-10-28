package io.krugosvet.dailydish.android.ui.container

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.BindingContainer
import io.krugosvet.dailydish.android.architecture.BindingImpl
import io.krugosvet.dailydish.android.architecture.setVisibility
import io.krugosvet.dailydish.android.databinding.ActivityContainerBinding
import io.krugosvet.dailydish.android.errorHandler
import io.krugosvet.dailydish.android.ui.container.ContainerViewModel.Event
import io.krugosvet.dailydish.android.ui.mealList.MealListFragmentDirections
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus

@AndroidEntryPoint
class ContainerActivity :
    AppCompatActivity(),
    BindingContainer<ActivityContainerBinding, ContainerViewModel> {

    override val viewModel: ContainerViewModel by viewModels()

    override val bindingComponent = BindingImpl(R.layout.activity_container, this)

    private val navController: NavController by lazy { findNavController(R.id.hostFragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.navigationEvent
            .flowWithLifecycle(lifecycle)
            .onEach { event ->
                when (event) {
                    Event.ShowAddMeal -> showAddMeal()
                }
            }
            .launchInCatching()
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

    private fun <T> Flow<T>.launchInCatching(): Job =
        launchIn(lifecycleScope + errorHandler)
}
