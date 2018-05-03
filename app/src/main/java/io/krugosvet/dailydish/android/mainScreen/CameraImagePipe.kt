package io.krugosvet.dailydish.android.mainScreen

interface CameraImagePipe {

    fun openCamera(callback: (str: String) -> Unit)
}
