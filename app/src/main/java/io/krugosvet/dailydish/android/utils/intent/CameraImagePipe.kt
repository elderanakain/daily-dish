package io.krugosvet.dailydish.android.utils.intent

import android.net.Uri

interface CameraImagePipe {

  fun openMealMainImageUpdateDialog(
    onPhotoReceiveCallback: (Uri) -> Unit,
    onPhotoDelete: () -> Unit,
    mainImageIsEmpty: Boolean
  )
}
