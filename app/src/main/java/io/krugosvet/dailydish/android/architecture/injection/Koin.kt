package io.krugosvet.dailydish.android.architecture.injection

import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import io.krugosvet.dailydish.android.architecture.view.GenericBaseActivity
import io.krugosvet.dailydish.android.screen.addMeal.viewmodel.AddMealViewModel
import io.krugosvet.dailydish.android.screen.container.view.ContainerActivity
import io.krugosvet.dailydish.android.screen.mealList.view.MealVisualFactory
import io.krugosvet.dailydish.android.screen.mealList.viewmodel.MealListViewModel
import io.krugosvet.dailydish.android.service.ImageService
import io.krugosvet.dailydish.android.service.KeyboardService
import io.krugosvet.dailydish.android.service.PreferenceService
import io.krugosvet.dailydish.android.service.SnackbarService
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

val module = module {

  scope<ContainerActivity> {
    scoped {
      SnackbarService(get())
    }

    scoped {
      ImageService(get(), get())
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

  viewModel {
    AddMealViewModel(get(), get(), get())
  }

  viewModel {
    MealListViewModel(get(), get(), get(), get())
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
