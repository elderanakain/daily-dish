package io.krugosvet.dailydish.common.core

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

public val currentDate: LocalDate
    get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

public val LocalDate.isToday: Boolean
    get() = this == currentDate

public fun LocalDate.toDisplayString(): String =
    toString().replace("-", " ")
