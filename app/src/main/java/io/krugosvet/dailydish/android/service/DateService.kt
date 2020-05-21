package io.krugosvet.dailydish.android.service

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

class DateService {

  val currentDate: Date
    get() = Calendar.getInstance().time

  fun toDate(millis: Long) = Date(millis)

  fun getLongFormattedDate(date: Date): String =
    DateFormat.getDateInstance(DateFormat.LONG).format(date)

  /**
   * @param date takes string in "yyyy-MM-dd HH:mm:ss" format
   * @return parsed date
   */
  fun defaultFormatDate(date: String): Date =
    try {
      getSimpleDefaultDateFormat().parse(date)!!
    } catch (e: ParseException) {
      currentDate
    }

  /**
   * @param year to parse
   * @param month of the year to parse
   * @param day of the month to parse
   * @return parse date with current time in "yyyy-MM-dd HH:mm:ss" format
   */
  fun defaultFormatDate(year: Int, month: Int, day: Int): Date =
    getSimpleDefaultDateFormat().parse("$year-$month-$day ${currentFormattedTime()}")!!

  fun isCurrentDate(date: Date): Boolean {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    return formatter.format(date) == formatter.format(currentDate)
  }

  /**
   * @return SimpleDateFormat with default locale and default timezone in "yyyy-MM-dd HH:mm:ss" format
   */
  private fun getSimpleDefaultDateFormat() =
    SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault()).apply {
      timeZone = TimeZone.getDefault()
    }

  /**
   * @return current time in "HH:mm:ss" format
   */
  private fun currentFormattedTime(): String =
    with(Calendar.getInstance()) { "${hours()}:${minutes()}:${seconds()}" }

  private fun Calendar.hours() = this.get(Calendar.HOUR_OF_DAY)
  private fun Calendar.minutes() = this.get(Calendar.MINUTE)
  private fun Calendar.seconds() = this.get(Calendar.SECOND)
}
