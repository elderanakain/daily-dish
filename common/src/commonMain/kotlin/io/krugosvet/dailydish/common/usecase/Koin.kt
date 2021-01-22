package io.krugosvet.dailydish.common.usecase

import org.koin.dsl.module

internal val useCaseModule = module {

  factory {
    AddMealUseCase(get())
  }

  factory {
    DeleteMealUseCase(get())
  }

  factory {
    ChangeMealImageUseCase(get())
  }

  factory {
    SetCurrentTimeToCookedDateMealUseCase(get())
  }
}
