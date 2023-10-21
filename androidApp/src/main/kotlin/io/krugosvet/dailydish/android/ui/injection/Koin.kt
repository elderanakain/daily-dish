package io.krugosvet.dailydish.android.ui.injection

import io.krugosvet.dailydish.android.ui.addMeal.AddMealViewModel
import io.krugosvet.dailydish.android.ui.addMeal.AddMealVisualFactory
import io.krugosvet.dailydish.android.ui.addMeal.AddMealVisualValidator
import io.krugosvet.dailydish.android.ui.container.ContainerViewModel
import io.krugosvet.dailydish.android.ui.mealList.MealListDecorator
import io.krugosvet.dailydish.android.ui.mealList.MealListViewModel
import io.krugosvet.dailydish.android.ui.mealList.MealVisualFactory
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val uiModule = module {
    singleOf(::MealVisualFactory)
    factoryOf(::MealListDecorator)
    factoryOf(::AddMealVisualFactory)
    factoryOf(::AddMealVisualValidator)
    viewModelOf(::AddMealViewModel)
    viewModelOf(::MealListViewModel)
    viewModelOf(::ContainerViewModel)
}
