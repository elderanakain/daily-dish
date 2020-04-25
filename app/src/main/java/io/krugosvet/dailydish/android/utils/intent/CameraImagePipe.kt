package io.krugosvet.dailydish.android.utils.intent

import android.net.*

interface CameraImagePipe {

  fun openMealMainImageUpdateDialog(
      onPhotoReceiveCallback: (Uri) -> Unit,
      onPhotoDelete: () -> Unit,
      mainImageIsEmpty: Boolean
  )
}
