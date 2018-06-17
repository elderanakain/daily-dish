package io.krugosvet.dailydish.android.utils

import java.text.DateFormat.LONG
import java.text.DateFormat.getDateInstance
import java.util.*
import java.util.concurrent.TimeUnit

fun getCurrentDateString(): String = getDateInstance(LONG).format(Calendar.getInstance().time)

fun getCurrentDate() = Calendar.getInstance().time

fun getFormattedDate(year: Int, monthOfYear: Int, dayOfMonth: Int): String =
        getDateInstance(LONG).format(Calendar.getInstance().also {
            it.set(year, monthOfYear, dayOfMonth)
        }.time)

fun getFormattedDate(date: Date?): String = getDateInstance(LONG).format(date)

/**
 * @param date takes LONG date format, e.g. May 2, 2018.
 */
fun parseDate(date: String): Date = getDateInstance(LONG).parse(date).apply {
    val calendar = Calendar.getInstance()
    this.time += (TimeUnit.HOURS.toMillis(calendar.get(Calendar.HOUR_OF_DAY).toLong())
            + TimeUnit.MINUTES.toMillis(calendar.get(Calendar.MINUTE).toLong())
            + TimeUnit.SECONDS.toMillis(calendar.get(Calendar.SECOND).toLong()))
}
