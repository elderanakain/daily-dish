package io.krugosvet.dailydish.android.dagger

import dagger.*
import io.krugosvet.dailydish.android.dagger.module.*
import io.krugosvet.dailydish.android.mainScreen.*
import io.krugosvet.dailydish.android.mainScreen.tab.*
import io.krugosvet.dailydish.android.utils.baseUi.*
import javax.inject.*

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
  fun inject(activity: BaseActivity)
  fun inject(activity: StartupActivity)
  fun inject(fragment: MealListPageFragment)
  fun inject(adapter: MealListAdapter)
}
