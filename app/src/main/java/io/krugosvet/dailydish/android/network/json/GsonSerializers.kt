package io.krugosvet.dailydish.android.network.json

import android.net.Uri
import android.util.Base64
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.krugosvet.dailydish.android.DailyDishApplication
import org.apache.commons.io.IOUtils

class MainImageTypeAdapter : TypeAdapter<String>() {
    override fun write(out: JsonWriter?, value: String?) {
        if (out != null && value != null) {
            if (!value.isEmpty()) {
                DailyDishApplication.appContext.contentResolver
                        .openInputStream(Uri.parse(value)).use {
                            out.value(Base64.encodeToString(IOUtils.toByteArray(it), Base64.NO_WRAP))
                        }
            } else {
                out.value("")
            }
        }
    }

    override fun read(`in`: JsonReader?): String {
        return if (`in`?.hasNext() == true) `in`.nextString() else ""
    }
}
