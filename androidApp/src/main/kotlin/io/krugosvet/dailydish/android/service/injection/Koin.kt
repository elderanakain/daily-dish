package io.krugosvet.dailydish.android.service.injection

import androidx.preference.PreferenceManager
import io.krugosvet.dailydish.android.service.DialogService
import io.krugosvet.dailydish.android.service.PreferenceService
import io.krugosvet.dailydish.android.ui.container.ContainerActivity
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module

val serviceModule = module {

    scope<ContainerActivity> {
        scopedOf(::DialogService)
    }

    single {
        PreferenceService(PreferenceManager.getDefaultSharedPreferences(get()), get())
    }
}
