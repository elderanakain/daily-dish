package io.krugosvet.dailydish.android.network.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import io.krugosvet.dailydish.android.utils.getCurrentDate
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

internal class DateDeserializer : JsonDeserializer<Date> {

    override fun deserialize(element: JsonElement, arg1: Type, arg2: JsonDeserializationContext): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")

        return try {
            formatter.parse(element.asString)
        } catch (e: ParseException) {
            getCurrentDate()
        }
    }
}
