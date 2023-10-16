package io.krugosvet.dailydish.android.ui.injection

import androidx.lifecycle.SavedStateHandle
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealVisualFactory
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealVisualValidator
import io.krugosvet.dailydish.android.ui.addMeal.viewmodel.AddMealViewModel
import io.krugosvet.dailydish.android.ui.container.viewmodel.ContainerViewModel
import io.krugosvet.dailydish.android.ui.mealList.MealListDecorator
import io.krugosvet.dailydish.android.ui.mealList.MealVisualFactory
import io.krugosvet.dailydish.android.ui.mealList.MealListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {

    single {
        MealVisualFactory(get())
    }

    factory {
        MealListDecorator(get())
    }

    factory {
        AddMealVisualFactory(get())
    }

    factory {
        AddMealVisualValidator()
    }

    viewModel { (savedStateHandle: SavedStateHandle) ->
        AddMealViewModel(savedStateHandle, get(), get())
    }

    viewModel { (savedStateHandle: SavedStateHandle) ->
        MealListViewModel(savedStateHandle, get(), get(), get(), get(), get(), get())
    }

    viewModel { (savedStateHandle: SavedStateHandle) ->
        ContainerViewModel(savedStateHandle)
    }
}
