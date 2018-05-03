package io.krugosvet.dailydish.android.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun getNewImagePath(context: Context?): File {
    val timeStamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
    val pictureFile = "DailyDish$timeStamp"
    val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(pictureFile, ".jpg", storageDir)
}
