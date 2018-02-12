package io.krugosvet.dailydish.android.utils

import io.krugosvet.dailydish.android.db.objects.Meal
import io.realm.Realm
import io.realm.RealmResults

fun Realm.getMeals(): RealmResults<Meal> = this.where(Meal::class.java).findAll().sort("id")
