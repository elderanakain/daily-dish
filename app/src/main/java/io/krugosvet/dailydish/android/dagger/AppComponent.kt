package io.krugosvet.dailydish.android.dagger

import dagger.Component
import io.krugosvet.dailydish.android.dagger.module.AppModule
import io.krugosvet.dailydish.android.dagger.module.NetworkModule
import io.krugosvet.dailydish.android.mainScreen.DialogAddMeal
import io.krugosvet.dailydish.android.mainScreen.MealListAdapter
import io.krugosvet.dailydish.android.mainScreen.StartupActivity
import io.krugosvet.dailydish.android.mainScreen.tab.MealListPageFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {

  fun inject(activity: StartupActivity)

  fun inject(fragment: MealListPageFragment)

  fun inject(adapter: MealListAdapter)

  fun inject(fragment: DialogAddMeal)
}
