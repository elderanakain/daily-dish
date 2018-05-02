package io.krugosvet.dailydish.android.utils

import io.krugosvet.dailydish.android.db.objects.Meal
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

fun Realm.getMeals(): RealmResults<Meal> = this.where(Meal::class.java).findAll().sort("id")

fun Realm.getAscByDateMeals(): RealmResults<Meal> =
        this.where(Meal::class.java).sort("date", Sort.ASCENDING).findAll()
