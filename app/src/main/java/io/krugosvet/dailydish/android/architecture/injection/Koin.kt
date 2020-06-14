package io.krugosvet.dailydish.android.architecture.injection

import androidx.fragment.app.Fragment
import io.krugosvet.dailydish.android.architecture.view.GenericBaseActivity
import io.krugosvet.dailydish.android.screen.addMeal.viewmodel.AddMealViewModel
import io.krugosvet.dailydish.android.screen.container.view.ContainerActivity
import io.krugosvet.dailydish.android.screen.mealList.view.MealVisualFactory
import io.krugosvet.dailydish.android.screen.mealList.viewmodel.MealListViewModel
import io.krugosvet.dailydish.android.service.ImageService
import io.krugosvet.dailydish.android.service.KeyboardService
import io.krugosvet.dailydish.android.service.SnackbarService
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.androidx.scope.lifecycleScope
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.experimental.builder.scoped
import org.koin.experimental.builder.single
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@Suppress("RemoveExplicitTypeArguments")
val module = module {

  scope<ContainerActivity> {
    scoped<SnackbarService>()
    scoped<ImageService>()
    scoped<KeyboardService>()
  }

  single<MealVisualFactory>()

  viewModel<AddMealViewModel>()
  viewModel<MealListViewModel>()
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
