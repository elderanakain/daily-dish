package io.krugosvet.dailydish.android.dagger

import dagger.Subcomponent
import dagger.android.AndroidInjector
import io.krugosvet.dailydish.android.mainScreen.StartupActivity

@Subcomponent()
public interface StartupActivitySubcomponent: AndroidInjector<StartupActivity> {
    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<StartupActivity>() {}
}
