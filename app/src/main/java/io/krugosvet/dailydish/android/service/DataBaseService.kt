package io.krugosvet.dailydish.android.service

import io.krugosvet.dailydish.android.db.meal.MealEntity
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.deleteFromRealm
import io.realm.kotlin.isValid

class DataBaseService(
  private val dateService: DateService
) {

  private val realm
    get() = Realm.getDefaultInstance()

  val meals: RealmResults<MealEntity>
    get() = realm.where(MealEntity::class.java).findAll()

  fun persist(mealEntity: MealEntity) =
    doInRealm {
      val localIMainImage = File(mealEntity.imageUri)
      if (localIMainImage.exists()) {
        localIMainImage.delete()
      }
      copyToRealmOrUpdate(mealEntity)
    }

  fun delete(mealEntity: MealEntity) =
    doInRealm(mealEntity.isValid()) {
      mealEntity.deleteFromRealm()
    }

  fun updateDateToCurrent(mealEntity: MealEntity) =
    doInRealm(mealEntity.isValid()) {
      mealEntity.lastCookingDate = dateService.currentDate
    }

  fun changeImage(mealEntity: MealEntity, image: String) =
    doInRealm {
      mealEntity.imageUri = image
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
