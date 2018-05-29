package io.krugosvet.dailydish.android.dagger

import dagger.Component
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import io.krugosvet.dailydish.android.DailyDishApplication
import io.krugosvet.dailydish.android.network.MealServicePipe
import io.krugosvet.dailydish.android.utils.baseUi.BaseActivity
import io.krugosvet.dailydish.android.utils.baseUi.BaseFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [BaseActivityModule::class, BaseFragmentModule::class, AndroidSupportInjectionModule::class])
interface DailyDishApplicationComponent {
    fun inject(application: DailyDishApplication)
}

@Subcomponent()
interface BaseActivitySubcomponent : AndroidInjector<BaseActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<BaseActivity>()
}

@Subcomponent()
interface BaseFragmentSubcomponent : AndroidInjector<BaseFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<BaseFragment>()
}

@Component(modules = [NetworkModule::class, AccountModule::class])
interface MealServicePipeComponent {
    fun inject(mealServicePipe: MealServicePipe)
}
