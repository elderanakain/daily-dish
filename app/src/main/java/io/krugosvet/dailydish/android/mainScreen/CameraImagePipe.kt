package io.krugosvet.dailydish.android.mainScreen

import java.io.File

interface CameraImagePipe {

    fun openMealMainImageUpdateDialog(onPhotoReceiveCallback: (File?) -> Unit,
                                      onPhotoDelete: () -> Unit, mainImageIsEmpty: Boolean)
}
