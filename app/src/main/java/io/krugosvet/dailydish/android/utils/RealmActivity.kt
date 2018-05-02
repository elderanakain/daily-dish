package io.krugosvet.dailydish.android.utils

import android.support.v7.app.AppCompatActivity
import io.realm.Realm

abstract class RealmActivity: AppCompatActivity() {

    val realm: Realm = Realm.getDefaultInstance()

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
