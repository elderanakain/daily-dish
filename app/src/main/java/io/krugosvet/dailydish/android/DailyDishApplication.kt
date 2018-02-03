package io.krugosvet.dailydish.android

import android.app.Application
import io.realm.Realm

class DailyDishApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}