package io.krugosvet.dailydish.android.utils

import java.text.DateFormat.LONG
import java.text.DateFormat.getDateInstance
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

fun getCurrentDate(): Date = Calendar.getInstance().time

fun getLongFormattedDate(date: Date?): String = getDateInstance(LONG).format(date)

/**
 * @param date takes string in "yyyy-MM-dd HH:mm:ss" format
 * @return parsed date
 */
fun defaultFormatDate(date: String): Date =
    try {
      getSimpleDefaultDateFormat().parse(date)
    } catch (e: ParseException) {
      getCurrentDate()
    }

/**
 * @param year to parse
 * @param month of the year to parse
 * @param day of the month to parse
 * @return parse date with current time in "yyyy-MM-dd HH:mm:ss" format
 */
fun defaultFormatDate(year: Int, month: Int, day: Int): Date =
    getSimpleDefaultDateFormat().parse("$year-$month-$day ${currentFormattedTime()}")

fun isCurrentDate(date: Date): Boolean {
  val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

  return formatter.format(date) == formatter.format(getCurrentDate())
}

/**
 * @return SimpleDateFormat with default locale and default timezone in "yyyy-MM-dd HH:mm:ss" format
 */
private fun getSimpleDefaultDateFormat(): SimpleDateFormat {
  val formatter = SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault())
  formatter.timeZone = TimeZone.getDefault()

  return formatter
}

/**
 * @return current time in "HH:mm:ss" format
 */
private fun currentFormattedTime(): String {
  val c = Calendar.getInstance()

  return "${c.hours()}:${c.minutes()}:${c.seconds()}"
}

private fun Calendar.hours() = this.get(Calendar.HOUR_OF_DAY)
private fun Calendar.minutes() = this.get(Calendar.MINUTE)
private fun Calendar.seconds() = this.get(Calendar.SECOND)
