package io.krugosvet.dailydish.android.utils

import android.content.Context
import android.os.Environment
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
