package io.krugosvet.dailydish.android.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*



@Throws(IOException::class)
fun createImageFile(context: Context): File = File.createTempFile("JPEG_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date()) + "_",
        ".jpg", context.getExternalFilesDir(Environment.DIRECTORY_PICTURES))

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

fun bytesFromBitmap(bitmap: Bitmap?): Single<ByteArray> {
    return Single.create(SingleOnSubscribe<ByteArray> { e -> e.onSuccess(compressBitmap(bitmap)) })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun compressBitmap(bitmap: Bitmap?): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, stream)
    val byteArray = stream.toByteArray()
    return byteArray ?: byteArrayOf()
}
