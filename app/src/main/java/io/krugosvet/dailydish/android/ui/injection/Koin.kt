package io.krugosvet.dailydish.android.ui.injection

import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealFormValidator
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealVisualFactory
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealVisualValidator
import io.krugosvet.dailydish.android.ui.addMeal.viewmodel.AddMealViewModel
import io.krugosvet.dailydish.android.ui.container.viewmodel.ContainerViewModel
import io.krugosvet.dailydish.android.ui.mealList.view.MealListDecorator
import io.krugosvet.dailydish.android.ui.mealList.view.MealVisualFactory
import io.krugosvet.dailydish.android.ui.mealList.viewmodel.MealListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {

  single {
    MealVisualFactory(get(), get())
  }

  factory {
    MealListDecorator(get())
  }

  factory {
    AddMealVisualFactory(get(), get())
  }

  factory {
    AddMealVisualValidator()
  }

  factory {
    AddMealFormValidator()
  }

  viewModel {
    AddMealViewModel(get(), get(), get())
  }

  viewModel {
    MealListViewModel(get(), get(), get(), get(), get(), get(), get())
  }

  viewModel {
    ContainerViewModel(get())
  }
}
