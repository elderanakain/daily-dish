package io.krugosvet.dailydish.android.utils

import io.krugosvet.dailydish.android.db.objects.Meal
import io.realm.Realm

fun Realm.getMeals() = this.where(Meal::class.java).findAll().sort("id")
