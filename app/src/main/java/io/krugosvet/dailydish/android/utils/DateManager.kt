package io.krugosvet.dailydish.android.utils

import java.text.DateFormat.LONG
import java.text.DateFormat.getDateInstance
import java.util.*
import java.util.Calendar.*
import java.util.concurrent.TimeUnit

fun getCurrentDate(): String = getDateInstance(LONG).format(getInstance().time)

fun getFormattedDate(year: Int, monthOfYear: Int, dayOfMonth: Int): String =
        getDateInstance(LONG).format(getInstance().also {
            it.set(year, monthOfYear, dayOfMonth)
        }.time)

fun getFormattedDate(date: Date?): String = getDateInstance(LONG).format(date)

fun parseDate(date: String): Date = getDateInstance(LONG).parse(date).apply {
    val calendar = getInstance()
    this.time += (TimeUnit.HOURS.toMillis(calendar.get(HOUR_OF_DAY).toLong())
            + TimeUnit.MINUTES.toMillis(calendar.get(MINUTE).toLong())
            + TimeUnit.SECONDS.toMillis(calendar.get(SECOND).toLong()))
}
