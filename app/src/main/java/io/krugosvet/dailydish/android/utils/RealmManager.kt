package io.krugosvet.dailydish.android.utils

import android.support.v7.widget.RecyclerView
import io.krugosvet.dailydish.android.db.objects.Meal
import io.realm.*

fun Realm.getAscByDateMeals(userId: String): RealmQuery<Meal> {
    val query = this.where(Meal::class.java).sort("date", Sort.ASCENDING)
    return if (userId.isEmpty()) query else query.equalTo("userId", userId)
}

fun Realm.getDescByDateMeals(userId: String): RealmQuery<Meal> {
    val query = this.where(Meal::class.java).sort("date", Sort.DESCENDING)
    return if (userId.isEmpty()) query else query.equalTo("userId", userId)
}

fun <T : RealmModel, S : RecyclerView.ViewHolder, L : OrderedRealmCollection<T>>
        RealmRecyclerViewAdapter<T, S>.addListener(listener: RealmChangeListener<L>) {
    if (data != null) {
        when (data) {
            is RealmResults<T> -> (data as RealmResults<T>).addChangeListener(listener as RealmChangeListener<RealmResults<T>>)
            is RealmList<T> -> (data as RealmList<T>).addChangeListener(listener as RealmChangeListener<RealmList<T>>)
            else -> throw IllegalArgumentException("RealmCollection not supported: " + data?.javaClass)
        }
    }
}

fun <T : RealmModel, S : RecyclerView.ViewHolder, L : OrderedRealmCollection<T>>
        RealmRecyclerViewAdapter<T, S>.removeListener(listener: RealmChangeListener<L>) {
    if (data != null) {
        when (data) {
            is RealmResults<T> -> (data as RealmResults<T>).removeChangeListener(listener as RealmChangeListener<RealmResults<T>>)
            is RealmList<T> -> (data as RealmList<T>).removeChangeListener(listener as RealmChangeListener<RealmList<T>>)
            else -> throw IllegalArgumentException("RealmCollection not supported: " + data?.javaClass)
        }
    }
}
