package io.krugosvet.dailydish.android.db.objects

import io.krugosvet.dailydish.android.utils.getMeals
import io.krugosvet.dailydish.android.utils.readBytesFromFile
import io.realm.Realm
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required
import io.realm.kotlin.deleteFromRealm
import java.io.File
import java.util.*

@RealmClass
open class Meal @JvmOverloads constructor(
        @Required var title: String = "",
        @Required var description: String = "",
        @Required var date: Date = Date(),
        var mainImage: ByteArray = byteArrayOf()) : RealmModel {

    @PrimaryKey
    var id = 0

    fun persist(realm: Realm) {
        val meals = realm.getMeals()
        id = if (meals.isEmpty()) 0 else meals.last()!!.id + 1

        realm.executeTransaction {
            it.copyToRealmOrUpdate(this)
        }
    }

    fun delete(realm: Realm) {
        realm.executeTransaction {
            this.deleteFromRealm()
        }
    }

    fun changeMainImage(realm: Realm, file: File?) {
        realm.executeTransaction {
            this.mainImage = readBytesFromFile(file)
        }
    }

    fun removeMainImage(realm: Realm) {
        realm.executeTransaction {
            this.mainImage = byteArrayOf()
        }
    }
}
