package io.krugosvet.dailydish.android.utils

import android.content.*
import android.graphics.*
import android.os.*
import io.reactivex.*
import io.reactivex.android.schedulers.*
import io.reactivex.schedulers.*
import java.io.*
import java.text.*
import java.util.*

@Throws(IOException::class)
fun createImageFile(context: Context): File =
    File.createTempFile(
        "JPEG_"
            + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            + "_",
        ".jpg", context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    )

fun readBytesFromFile(file: File?): ByteArray {
  var fileInputStream: FileInputStream? = null
  var bytesArray: ByteArray = byteArrayOf()

  try {
    bytesArray = ByteArray(file?.length()?.toInt() ?: 0)

    fileInputStream = FileInputStream(file)
    fileInputStream.read(bytesArray)

  } catch (e: IOException) {
    e.printStackTrace()
  } finally {
    fileInputStream?.close()
  }

  return bytesArray
}

fun bytesFromBitmap(bitmap: Bitmap?): Single<ByteArray> =
    Single.create(SingleOnSubscribe<ByteArray> { e -> e.onSuccess(compressBitmap(bitmap)) })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun compressBitmap(bitmap: Bitmap?): ByteArray {
  val stream = ByteArrayOutputStream()
  bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, stream)
  val byteArray = stream.toByteArray()

  return byteArray ?: byteArrayOf()
}
