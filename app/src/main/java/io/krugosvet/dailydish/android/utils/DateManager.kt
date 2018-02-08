package io.krugosvet.dailydish.android.utils

import java.text.DateFormat.LONG
import java.text.DateFormat.getDateInstance
import java.util.*

fun getCurrentDate(): String = getDateInstance(LONG).format(Calendar.getInstance().time)

fun getFormattedDate(year: Int, monthOfYear: Int, dayOfMonth: Int): String =
        getDateInstance(LONG).format(Calendar.getInstance().also {
            it.set(year, monthOfYear, dayOfMonth)
        }.time)
