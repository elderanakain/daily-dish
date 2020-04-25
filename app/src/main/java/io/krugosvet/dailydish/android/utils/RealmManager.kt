package io.krugosvet.dailydish.android.utils

import androidx.recyclerview.widget.*
import io.krugosvet.dailydish.android.db.objects.meal.*
import io.realm.*

fun Realm.getAscByDateMeals(): RealmQuery<Meal> {
  val query = where(Meal::class.java)
      .sort("date", Sort.ASCENDING)
      .notEqualTo("id", -1 as Int)

  return if (true) query else query.equalTo("userId", "")
}

fun Realm.getDescByDateMeals(): RealmQuery<Meal> {
  val query = where(Meal::class.java)
      .sort("date", Sort.DESCENDING)
      .notEqualTo("id", -1 as Int)

  return if (true) query else query.equalTo("userId", "")
}

fun <
    T : RealmModel,
    S : RecyclerView.ViewHolder,
    L : OrderedRealmCollection<T>
    >
    RealmRecyclerViewAdapter<T, S>.addListener(listener: RealmChangeListener<L>) {

  if (data != null) {
    when (data) {
      is RealmResults<T> -> {
        (data as RealmResults<T>).addChangeListener(
            listener as RealmChangeListener<RealmResults<T>>
        )
      }
      is RealmList<T> -> {
        (data as RealmList<T>).addChangeListener(listener as RealmChangeListener<RealmList<T>>)
      }
      else -> {
        throw IllegalArgumentException("RealmCollection not supported: " + data?.javaClass)
      }
    }
  }
}

fun <
    T : RealmModel,
    S : RecyclerView.ViewHolder,
    L : OrderedRealmCollection<T>
    >
    RealmRecyclerViewAdapter<T, S>.removeListener(listener: RealmChangeListener<L>) {

  if (data != null) {
    when (data) {
      is RealmResults<T> -> {
        (data as RealmResults<T>).removeChangeListener(
            listener as RealmChangeListener<RealmResults<T>>
        )
      }
      is RealmList<T> -> {
        (data as RealmList<T>).removeChangeListener(listener as RealmChangeListener<RealmList<T>>)
      }
      else -> {
        throw IllegalArgumentException("RealmCollection not supported: " + data?.javaClass)
      }
    }
  }
}
