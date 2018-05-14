package io.krugosvet.dailydish.android.dagger

import dagger.Component
import io.krugosvet.dailydish.android.DailyDishApplication

@Component(modules = [StartupActivityModule::class])
interface DailyDishApplicationComponent {
    fun inject(application: DailyDishApplication)
}
