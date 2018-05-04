package io.krugosvet.dailydish.android.mainScreen

import java.io.File

interface CameraImagePipe {

    fun openImageProviderChooser(onPhotoReceiveCallback: (file: File?) -> Unit)
}
