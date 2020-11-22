package io.krugosvet.dailydish.android.usecase.injection

import io.krugosvet.dailydish.android.usecase.AddMealUseCase
import io.krugosvet.dailydish.android.usecase.DeleteMealUseCase
import io.krugosvet.dailydish.android.usecase.UpdateMealUseCase
import org.koin.dsl.module

val useCaseModule = module {

  factory {
    AddMealUseCase(get(), get(), get())
  }

  factory {
    DeleteMealUseCase(get())
  }

  factory {
    UpdateMealUseCase(get(), get())
  }
}
