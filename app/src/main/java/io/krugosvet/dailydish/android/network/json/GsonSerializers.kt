package io.krugosvet.dailydish.android.network.json

import android.net.*
import android.util.*
import com.google.gson.*
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.krugosvet.dailydish.android.*
import org.apache.commons.io.*

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
