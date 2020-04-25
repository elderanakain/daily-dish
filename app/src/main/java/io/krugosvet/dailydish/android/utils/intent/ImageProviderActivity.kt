package io.krugosvet.dailydish.android.utils.intent

import android.content.pm.*
import android.net.*
import io.krugosvet.dailydish.android.utils.baseUi.*

abstract class ImageProviderActivity : BaseActivity(), CameraImagePipe {

  private var imageProvider: ImageProvider? = null

  override fun onRequestPermissionsResult(
      requestCode: Int, permissions: Array<out String>, grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    if (
        requestCode == REQUEST_PERMISSION_CAMERA
        && grantResults[0] == PackageManager.PERMISSION_GRANTED
    ) {
      imageProvider?.getImageFromCamera()
    }
  }

  override fun openMealMainImageUpdateDialog(
      onPhotoReceiveCallback: (Uri) -> Unit, onPhotoDelete: () -> Unit, mainImageIsEmpty: Boolean
  ) {
    imageProvider = ImageProvider(onPhotoReceiveCallback, onPhotoDelete, mainImageIsEmpty, this)
  }
}
