package io.krugosvet.dailydish.android.dagger

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import io.krugosvet.dailydish.android.mainScreen.StartupActivity

@Module(subcomponents = [(StartupActivitySubcomponent::class)])
internal abstract class StartupActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(StartupActivity::class)
    internal abstract fun bindYourActivityInjectorFactory(builder: StartupActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>
}
