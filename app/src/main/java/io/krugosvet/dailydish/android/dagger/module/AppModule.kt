package io.krugosvet.dailydish.android.dagger.module

import android.content.Context
import android.support.annotation.NonNull
import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

@Module
class AppModule(private val appContext: Context) {

    @Provides
    @NonNull
    @Singleton
    fun provideContext() = appContext

    @Provides
    @NonNull
    @Singleton
    fun provideRealm(): Realm {
        return Realm.getDefaultInstance()
    }
}
