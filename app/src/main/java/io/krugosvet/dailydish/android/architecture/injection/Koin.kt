package io.krugosvet.dailydish.android.architecture.injection

import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.preference.PreferenceManager
import io.krugosvet.dailydish.android.architecture.view.GenericBaseActivity
import io.krugosvet.dailydish.android.service.ImagePickerService
import io.krugosvet.dailydish.android.service.KeyboardService
import io.krugosvet.dailydish.android.service.PreferenceService
import io.krugosvet.dailydish.android.service.SnackbarService
import io.krugosvet.dailydish.android.ui.addMeal.viewmodel.AddMealViewModel
import io.krugosvet.dailydish.android.ui.container.view.ContainerActivity
import io.krugosvet.dailydish.android.ui.container.viewmodel.ContainerViewModel
import io.krugosvet.dailydish.android.ui.mealList.view.MealListDecorator
import io.krugosvet.dailydish.android.ui.mealList.view.MealVisualFactory
import io.krugosvet.dailydish.android.ui.mealList.viewmodel.MealListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@ExperimentalCoroutinesApi
@FlowPreview
val module = module {

  scope<ContainerActivity> {
    scoped {
      SnackbarService(get())
    }

    scoped {
      ImagePickerService(get(), get())
    }

    scoped {
      KeyboardService(get())
    }
  }

  single {
    MealVisualFactory(get(), get())
  }

  single {
    PreferenceService(
      PreferenceManager.getDefaultSharedPreferences(get()),
      get()
    )
  }

  factory {
    MealListDecorator(get())
  }

  viewModel { (savedState: SavedStateHandle) ->
    AddMealViewModel(savedState, get(), get(), get())
  }

  viewModel { (savedState: SavedStateHandle) ->
    MealListViewModel(savedState, get(), get(), get(), get())
  }

  viewModel { (savedState: SavedStateHandle) ->
    ContainerViewModel(savedState)
  }
}

inline fun <reified T : Any> GenericBaseActivity.activityInject() =
  lifecycleScope.inject<T> {
    parametersOf(this)
  }

inline fun <reified T : Any> Fragment.activityInject() =
  object : ReadOnlyProperty<Any, T> {

    override fun getValue(thisRef: Any, property: KProperty<*>): T =
      with(requireActivity()) {
        lifecycleScope.get { parametersOf(this) }
      }
  }
