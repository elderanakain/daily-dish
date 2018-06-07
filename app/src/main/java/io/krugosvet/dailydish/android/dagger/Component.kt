package io.krugosvet.dailydish.android.dagger

import dagger.Component
import io.krugosvet.dailydish.android.mainScreen.MealListPageFragment
import io.krugosvet.dailydish.android.mainScreen.StartupActivity
import io.krugosvet.dailydish.android.utils.baseUi.BaseActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, AccountModule::class])
interface AppComponent {
    fun inject(activity: BaseActivity)
    fun inject(activity: StartupActivity)
    fun inject(fragment: MealListPageFragment)
}

//@Component(modules = [NetworkModule::class, AccountModule::class])
//interface MealServicePipeComponent {
//    fun inject(mealServicePipe: MealServicePipe)
//}
