package io.krugosvet.dailydish.android.db.objects

import com.google.gson.annotations.SerializedName
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
        @Required @SerializedName("title") var title: String = "",
        @Required @SerializedName("description") var description: String = "",
        @Required @SerializedName("date") var date: Date = Date(),
        @SerializedName("main_image") var mainImage: ByteArray = byteArrayOf(),
        @Required @SerializedName("user_id") var userId: String = "") : RealmModel {

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
