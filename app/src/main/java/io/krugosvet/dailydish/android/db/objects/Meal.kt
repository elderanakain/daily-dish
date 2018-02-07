package io.krugosvet.dailydish.android.db.objects

import io.realm.Realm
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required
import java.util.*

@RealmClass
open class Meal @JvmOverloads constructor(
        @Required var title: String = "",
        @Required var description: String = "",
        @Required var date: Date = Date()) : RealmModel {

    @PrimaryKey
    var id = 0

    fun persist(realm: Realm) {
        val meals = realm.where(this::class.java).findAll()
        id = if (meals.isEmpty()) 0 else meals.last()!!.id + 1

        realm.executeTransactionAsync {
            it.copyToRealmOrUpdate(this)
        }
    }
}
