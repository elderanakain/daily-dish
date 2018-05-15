package io.krugosvet.dailydish.android.utils

import io.krugosvet.dailydish.android.db.objects.Meal
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.RealmResults
import io.realm.Sort

fun Realm.getMeals(): RealmResults<Meal> = this.where(Meal::class.java).findAll().sort("id")

fun Realm.getAscByDateMeals(userId: String): RealmQuery<Meal> =
        this.where(Meal::class.java).equalTo("userId", userId)
                .sort("date", Sort.ASCENDING)
