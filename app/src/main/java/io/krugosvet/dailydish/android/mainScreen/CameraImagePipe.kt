package io.krugosvet.dailydish.android.mainScreen

import java.io.File

interface CameraImagePipe {

    fun openCamera(callback: (file: File?) -> Unit)
}
