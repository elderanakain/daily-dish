package io.krugosvet.dailydish.android.utils.intent

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.support.annotation.StringRes
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.mainScreen.ImageProviderMainAction
import io.krugosvet.dailydish.android.mainScreen.ImageProviderSource
import io.krugosvet.dailydish.android.mainScreen.getImageProviderMainActionNames
import io.krugosvet.dailydish.android.mainScreen.getImageProviderNames
import io.krugosvet.dailydish.android.utils.image.uriToBitmapMapper
import io.reactivex.schedulers.Schedulers


class ImageProvider @JvmOverloads constructor(
        private val onPhotoReceiveCallback: (image: Bitmap) -> Unit = {},
        private val onPhotoDelete: () -> Unit,
        mainImageIsEmpty: Boolean, private val hostActivity: Activity) {

    init {
        if (mainImageIsEmpty) {
            openImageProviderChooser()
        } else {
            AlertDialog.Builder(hostActivity).setItems(getImageProviderMainActionNames()) { dialog, which ->
                when (ImageProviderMainAction.values()[which]) {
                    ImageProviderMainAction.UPDATE -> openImageProviderChooser()
                    ImageProviderMainAction.REMOVE -> onPhotoDelete.invoke()
                }
                dialog.dismiss()
            }.setNegativeButton(getString(R.string.dialog_cancel_button)) { dialog, _ -> dialog.dismiss() }
                    .create().show()
        }
    }

    fun getImageFromCamera() {
        if (ContextCompat.checkSelfPermission(hostActivity, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startImagePicker(Sources.CAMERA)
        } else {
            ActivityCompat.requestPermissions(hostActivity, arrayOf(Manifest.permission.CAMERA),
                    REQUEST_PERMISSION_CAMERA)
        }
    }

    private fun openImageProviderChooser() {
        AlertDialog.Builder(hostActivity).setItems(getImageProviderNames()) { dialog, which ->
            when (ImageProviderSource.values()[which]) {
                ImageProviderSource.CAMERA -> getImageFromCamera()
                ImageProviderSource.GALLERY -> startImagePicker(Sources.GALLERY)
            }
            dialog.dismiss()
        }.setNegativeButton(getString(R.string.dialog_cancel_button)) { dialog, _ -> dialog.dismiss() }
                .create().show()
    }

    private fun startImagePicker(sources: Sources) {
        RxImagePicker.with(hostActivity).requestImage(sources)
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .flatMap(uriToBitmapMapper(hostActivity)).subscribe { passReceivedImage(it) }
    }

    private fun getString(@StringRes resId: Int) = hostActivity.getString(resId)

    private fun passReceivedImage(image: Bitmap) = onPhotoReceiveCallback.invoke(image)
}
