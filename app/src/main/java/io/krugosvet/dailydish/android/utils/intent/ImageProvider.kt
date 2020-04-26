package io.krugosvet.dailydish.android.utils.intent

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mlsdev.rximagepicker.RxImageConverters
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.mainScreen.ImageProviderMainAction
import io.krugosvet.dailydish.android.mainScreen.ImageProviderSource
import io.krugosvet.dailydish.android.mainScreen.getImageProviderMainActionNames
import io.krugosvet.dailydish.android.mainScreen.getImageProviderNames
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class ImageProvider @JvmOverloads constructor(
  private val onPhotoReceiveCallback: (image: Uri) -> Unit = {},
  private val onPhotoDelete: () -> Unit,
  mainImageIsEmpty: Boolean,
  private val hostActivity: Activity
) {

  init {
    if (mainImageIsEmpty) {
      openImageProviderChooser()
    } else {
      AlertDialog.Builder(hostActivity)
        .setItems(getImageProviderMainActionNames()) { dialog, which ->
          when (ImageProviderMainAction.values()[which]) {
            ImageProviderMainAction.UPDATE -> openImageProviderChooser()
            ImageProviderMainAction.REMOVE -> onPhotoDelete.invoke()
          }
          dialog.dismiss()
        }
        .setNegativeButton(getString(R.string.dialog_cancel_button)) { dialog, _ -> dialog.dismiss() }
        .create()
        .show()
    }
  }

  fun getImageFromCamera() {
    if (
      ContextCompat.checkSelfPermission(hostActivity, Manifest.permission.CAMERA)
      == PackageManager.PERMISSION_GRANTED
    ) {
      startImagePicker(Sources.CAMERA)
    } else {
      ActivityCompat.requestPermissions(
        hostActivity, arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSION_CAMERA
      )
    }
  }

  private fun openImageProviderChooser() =
    AlertDialog.Builder(hostActivity)
      .setItems(getImageProviderNames()) { dialog, which ->
        when (ImageProviderSource.values()[which]) {
          ImageProviderSource.CAMERA -> getImageFromCamera()
          ImageProviderSource.GALLERY -> startImagePicker(Sources.GALLERY)
        }
        dialog.dismiss()
      }
      .setNegativeButton(getString(R.string.dialog_cancel_button)) { dialog, _ -> dialog.dismiss() }
      .create()
      .show()

  private fun startImagePicker(sources: Sources) {
    RxImagePicker.with(hostActivity)
      .requestImage(sources)
      .flatMap { RxImageConverters.uriToBitmap(hostActivity, it).observeOn(Schedulers.io()) }
      .flatMap { bitmap ->
        Observable.create(ObservableOnSubscribe<Uri> { emitter ->
          val compressedFileUri = createImageUri()
          hostActivity.contentResolver.openOutputStream(compressedFileUri).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, it)
          }

          emitter.onNext(compressedFileUri)
          emitter.onComplete()
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
      }
      .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
      .subscribe { passReceivedImage(it) }
  }

  private fun getString(@StringRes resId: Int) = hostActivity.getString(resId)

  private fun passReceivedImage(image: Uri) = onPhotoReceiveCallback.invoke(image)

  private fun createImageUri(): Uri {
    val contentResolver = hostActivity.contentResolver
    val cv = ContentValues()
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

    cv.put(MediaStore.Images.Media.TITLE, timeStamp)

    return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)!!
  }
}
