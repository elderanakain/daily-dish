package io.krugosvet.dailydish.android.utils.intent

import android.graphics.Bitmap

interface CameraImagePipe {

    fun openMealMainImageUpdateDialog(onPhotoReceiveCallback: (Bitmap) -> Unit,
                                      onPhotoDelete: () -> Unit, mainImageIsEmpty: Boolean)
}
