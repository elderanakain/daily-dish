package io.krugosvet.dailydish.android.db.objects

import android.graphics.Bitmap
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import io.krugosvet.dailydish.android.network.json.MainImageSerializer
import io.krugosvet.dailydish.android.utils.bytesFromBitmap
import io.realm.Realm
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required
import io.realm.kotlin.deleteFromRealm
import java.util.*
import javax.annotation.Nullable

@RealmClass
open class Meal @JvmOverloads constructor(
        @Required @SerializedName("title") var title: String = "",
        @Required @SerializedName("description") var description: String = "",
        @Required @SerializedName("date") var date: Date = Date(),
        @Nullable @JsonAdapter(MainImageSerializer::class) @SerializedName("main_image") var mainImage: ByteArray? = null,
        @Required @SerializedName("user_id") var userId: String = "") : RealmModel {

    @PrimaryKey
    var id = 0

    fun persist(realm: Realm, id: Int) {
        //TODO Uncomment for locally auto generated id
        //val meals = realm.getMeals()
        //id = if (meals.isEmpty()) 0 else meals.last()!!.id + 1

        this.id = id
        realm.executeTransaction {
            it.copyToRealmOrUpdate(this)
        }
    }

    fun delete(realm: Realm) {
        realm.executeTransaction {
            this.deleteFromRealm()
        }
    }

    fun changeMainImage(realm: Realm, bitmap: Bitmap) {
        bytesFromBitmap(bitmap).subscribe { byteArray ->
            realm.executeTransaction {
                this.mainImage = byteArray
            }
        }
    }

    fun removeMainImage(realm: Realm) {
        realm.executeTransaction {
            this.mainImage = byteArrayOf()
        }
    }
}
