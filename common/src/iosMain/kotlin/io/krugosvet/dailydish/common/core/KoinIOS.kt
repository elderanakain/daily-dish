package io.krugosvet.dailydish.common.core

import io.krugosvet.dailydish.common.repository.MealRepository
import org.koin.core.Koin

public val Koin.mealRepository: MealRepository
    get() = get()
