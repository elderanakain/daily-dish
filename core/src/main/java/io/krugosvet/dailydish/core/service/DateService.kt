package io.krugosvet.dailydish.core.service

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd"

class DateService {

  val currentDate: Calendar
    get() = Calendar.getInstance()

  val currentDateFormatted: String
    get() = currentDate.time.defaultFormatDate()

  val currentDateLongFormatted: String
    get() = getLongFormattedDate(currentDate.time)

  private fun Date.defaultFormatDate(): String =
    getSimpleDefaultDateFormat().format(this)

  fun getLongFormattedDate(date: Date): String =
    DateFormat.getDateInstance(DateFormat.LONG).format(date)

  fun getLongFormattedDate(date: String): String =
    DateFormat.getDateInstance(DateFormat.LONG).format(date.defaultFormatDate())

  /**
   * @param date takes string in "yyyy-MM-dd" format
   * @return parsed date
   */
  fun String.defaultFormatDate(): Date =
    try {
      getSimpleDefaultDateFormat().parse(this)!!
    } catch (e: ParseException) {
      currentDate.time
    }

  /**
   * @param year to parse
   * @param month of the year to parse
   * @param day of the month to parse
   * @return parse date with current time in "yyyy-MM-dd" format
   */
  fun defaultFormatDate(year: Int, month: Int, day: Int): Date =
    getSimpleDefaultDateFormat().parse("$year-$month-$day")!!

  fun format(year: Int, month: Int, day: Int): String = "$year-$month-$day"

  fun isCurrentDate(date: Date): Boolean {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    return formatter.format(date) == formatter.format(currentDate.time)
  }

  /**
   * @return SimpleDateFormat with default locale and default timezone in "yyyy-MM-dd" format
   */
  private fun getSimpleDefaultDateFormat() =
    SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault()).apply {
      timeZone = TimeZone.getDefault()
    }
}

val Calendar.year: Int
  get() = this.get(Calendar.YEAR)

val Calendar.month: Int
  get() = this.get(Calendar.MONTH)

val Calendar.day: Int
  get() = this.get(Calendar.DAY_OF_MONTH)
