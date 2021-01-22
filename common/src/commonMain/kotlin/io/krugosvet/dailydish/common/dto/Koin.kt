package io.krugosvet.dailydish.common.dto

import org.koin.dsl.module

internal val dtoModule = module {

  factory {
    AddMealFormFactory()
  }

  factory {
    AddMealFormValidator()
  }

  factory {
    MealFactory()
  }
}
