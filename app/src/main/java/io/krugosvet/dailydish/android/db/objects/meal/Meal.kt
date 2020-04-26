package io.krugosvet.dailydish.android.db.objects.meal

import android.net.Uri
import io.krugosvet.dailydish.android.utils.getCurrentDate
import io.realm.Realm
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required
import io.realm.kotlin.deleteFromRealm
import io.realm.kotlin.isValid
import java.io.File
import java.util.*

@RealmClass
open class Meal constructor(
  @Required var title: String = "",
  @Required var description: String = "",
  @Required var date: Date = Date(),
  @Required var mainImage: String = "",
  @Required var userId: String = ""
) :
  RealmModel {

  @PrimaryKey
  var id = 0

  fun persist(realm: Realm, id: Int) {
    this.id = id
    realm.executeTransaction {
      val localIMainImage = File(this.mainImage)
      if (localIMainImage.exists()) localIMainImage.delete()
      it.copyToRealmOrUpdate(this)
    }
  }

  fun delete(realm: Realm) =
    realm.executeTransaction {
      if (isValid()) deleteFromRealm()
    }

  fun changeMainImage(realm: Realm, uri: Uri) =
    realm.executeTransaction {
      this.mainImage = uri.toString()
    }

  fun removeMainImage(realm: Realm) =
    realm.executeTransaction {
      this.mainImage = ""
    }

  fun updateDateToCurrent(realm: Realm) {
    if (isValid()) {
      realm.executeTransaction {
        this.date = getCurrentDate()
      }
    }
  }

}
