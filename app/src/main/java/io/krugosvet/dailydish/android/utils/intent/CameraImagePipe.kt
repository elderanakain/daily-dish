package io.krugosvet.dailydish.android.utils.intent

import java.io.File

interface CameraImagePipe {

    fun openMealMainImageUpdateDialog(onPhotoReceiveCallback: (File?) -> Unit,
                                      onPhotoDelete: () -> Unit, mainImageIsEmpty: Boolean)
}
