package io.krugosvet.dailydish.android.service

import io.krugosvet.dailydish.android.db.Meal
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.deleteFromRealm
import io.realm.kotlin.isValid
import java.io.File

class DataBaseService(
  private val dateService: DateService
) {

  private val realm
    get() = Realm.getDefaultInstance()

  val meals: RealmResults<Meal>
    get() = realm.where(Meal::class.java).findAll()

  fun persist(meal: Meal) =
    doInRealm {
      val localIMainImage = File(meal.image)
      if (localIMainImage.exists()) {
        localIMainImage.delete()
      }
      copyToRealmOrUpdate(meal)
    }

  fun delete(meal: Meal) =
    doInRealm(meal.isValid()) {
      meal.deleteFromRealm()
    }

  fun updateDateToCurrent(meal: Meal) =
    doInRealm(meal.isValid()) {
      meal.date = dateService.currentDate
    }

  fun changeImage(meal: Meal, image: String) =
    doInRealm {
      meal.image = image
    }

  private inline fun doInRealm(
    shouldExecute: Boolean = true, crossinline action: Realm.() -> Unit
  ) {
    if (shouldExecute) {
      Realm
        .getDefaultInstance()
        .use {
          it.executeTransaction { realm ->
            action.invoke(realm)
          }
        }
    }
  }
}
