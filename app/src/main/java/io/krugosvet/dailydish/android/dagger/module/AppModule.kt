package io.krugosvet.dailydish.android.dagger.module

import android.content.*
import dagger.*
import io.realm.*
import javax.inject.*

@Module
class AppModule(
    private val appContext: Context
) {

  @Provides
  @Singleton
  fun provideContext() = appContext

  @Provides
  fun provideRealm(): Realm = Realm.getDefaultInstance()
}
