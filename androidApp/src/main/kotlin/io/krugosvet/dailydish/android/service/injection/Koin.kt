package io.krugosvet.dailydish.android.service.injection

import androidx.preference.PreferenceManager
import io.krugosvet.dailydish.android.service.PreferenceService
import org.koin.dsl.module

val serviceModule = module {
    single {
        PreferenceService(PreferenceManager.getDefaultSharedPreferences(get()), get())
    }
}
