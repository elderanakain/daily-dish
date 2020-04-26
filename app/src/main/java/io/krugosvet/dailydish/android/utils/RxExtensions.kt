package io.krugosvet.dailydish.android.utils

import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Maybe<T>.applySchedulers(): Maybe<T> = this.observeOn(AndroidSchedulers.mainThread())
  .subscribeOn(Schedulers.io())

fun <T> Single<T>.applySchedulers(): Single<T> = this.observeOn(AndroidSchedulers.mainThread())
  .subscribeOn(Schedulers.io())
