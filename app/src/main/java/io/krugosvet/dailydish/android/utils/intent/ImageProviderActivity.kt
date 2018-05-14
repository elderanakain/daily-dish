package io.krugosvet.dailydish.android.utils.intent

import android.content.Intent
import android.content.pm.PackageManager
import io.krugosvet.dailydish.android.utils.baseUi.BaseActivity
import java.io.File

abstract class ImageProviderActivity : BaseActivity(), CameraImagePipe {

    private var imageProvider: ImageProvider? = null

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            imageProvider?.startCamera()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CAPTURE_IMAGE -> imageProvider?.passLastReceivedImage()
                REQUEST_PICK_GALLERY_IMAGE -> imageProvider?.onActivityResultGalleryPick(data)
            }
        }
    }

    override fun openMealMainImageUpdateDialog(onPhotoReceiveCallback: (File?) -> Unit, onPhotoDelete: () -> Unit, mainImageIsEmpty: Boolean) {
        imageProvider = ImageProvider(onPhotoReceiveCallback, onPhotoDelete, mainImageIsEmpty, this)
    }
}
