@file:Suppress("RemoveExplicitTypeArguments")

package io.krugosvet.dailydish.android.architecture.injection

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.room.Room
import io.krugosvet.dailydish.android.architecture.view.GenericBaseActivity
import io.krugosvet.dailydish.android.db.DatabaseService
import io.krugosvet.dailydish.android.db.meal.MealDao
import io.krugosvet.dailydish.android.repository.Meal
import io.krugosvet.dailydish.android.repository.MealRepository
import io.krugosvet.dailydish.android.screen.addMeal.viewmodel.AddMealViewModel
import io.krugosvet.dailydish.android.screen.container.view.ContainerActivity
import io.krugosvet.dailydish.android.screen.mealList.view.MealVisualFactory
import io.krugosvet.dailydish.android.screen.mealList.viewmodel.MealListViewModel
import io.krugosvet.dailydish.android.service.*
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.androidx.scope.lifecycleScope
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.experimental.builder.scoped
import org.koin.experimental.builder.single
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

val module = module {

  scope<ContainerActivity> {
    scoped<SnackbarService>()
    scoped<ImageService>()
    scoped<KeyboardService>()
  }

  single<DateService>()
  single<ResourceService>()
  single<IdGenerator>()

  single<MealVisualFactory>()
  single<Meal.MealFactory>()
  single<MealRepository>()

  viewModel<AddMealViewModel>()
  viewModel<MealListViewModel>()
}

val dbModule = module {

  single<DatabaseService> {
    Room
      .databaseBuilder(get<Context>(), DatabaseService::class.java, "database")
      .build()
  }

  single<MealDao> {
    get<DatabaseService>().mealDao
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
