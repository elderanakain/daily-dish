package io.krugosvet.dailydish.android.network.json

import android.util.Base64
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class MainImageSerializer : JsonSerializer<ByteArray?> {
    override fun serialize(src: ByteArray?, typeOfSrc: Type?,
                           context: JsonSerializationContext?): JsonElement =
            JsonPrimitive(Base64.encodeToString(src, Base64.NO_WRAP))
}
