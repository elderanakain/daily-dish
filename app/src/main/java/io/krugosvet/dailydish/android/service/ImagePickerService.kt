package io.krugosvet.dailydish.android.service

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import androidx.annotation.StringRes
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.extension.sealedObjects
import io.krugosvet.dailydish.android.architecture.view.GenericBaseActivity
import io.krugosvet.dailydish.android.service.ImagePickerService.DialogSource.Action
import io.krugosvet.dailydish.android.service.ImagePickerService.DialogSource.Action.Remove
import io.krugosvet.dailydish.android.service.ImagePickerService.DialogSource.Action.Update
import io.krugosvet.dailydish.android.service.ImagePickerService.DialogSource.Source
import io.krugosvet.dailydish.core.service.ResourceService
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.rx2.asFlow
import java.util.concurrent.CancellationException

class ImagePickerService(
  private val activity: GenericBaseActivity,
  private val resources: ResourceService
) {

  sealed class DialogSource(@StringRes val text: Int) {

    sealed class Source(text: Int) : DialogSource(text) {
      object Camera : Source(R.string.take_a_picture)
      object Gallery : Source(R.string.pick_from_gallery)
    }

    sealed class Action(text: Int) : DialogSource(text) {
      object Update : Action(R.string.update_picture)
      object Remove : Action(R.string.remove_picture)
    }
  }

  fun showImagePicker(isImageEmpty: Boolean): Flow<Uri> = when {
    isImageEmpty -> openImageProviderPicker()
    else -> openActionDialog()
  }

  private fun openActionDialog(): Flow<Uri> =
    showDialog(Action::class.sealedObjects.toTypedArray())
      .flatMapConcat { source ->
        when (source) {
          Update -> openImageProviderPicker()
          Remove -> flowOf(Uri.EMPTY)
        }
      }

  private fun getImageFromCamera(): Flow<Uri> =
    checkPermission(Permission.Camera)
      .flatMapLatest {
        RxImagePicker.with(activity)
          .requestImage(Sources.CAMERA)
          .asFlow()
      }

  private fun getImageFromGallery(): Flow<Uri> =
    checkPermission(Permission.Gallery)
      .flatMapLatest {
        RxImagePicker.with(activity)
          .requestImage(Sources.GALLERY)
          .asFlow()
      }

  private fun checkPermission(permission: Permission): Flow<Int> =
    activity.permissionsObservable
      .drop(if (permission.assertGranted(activity)) 0 else 1)

  private fun openImageProviderPicker(): Flow<Uri> =
    showDialog(Source::class.sealedObjects.toTypedArray())
      .flatMapConcat { source ->
        when (source) {
          Source.Camera -> getImageFromCamera()
          Source.Gallery -> getImageFromGallery()
        }
      }

  private fun <T : DialogSource> showDialog(source: Array<T>): Flow<T> =
    callbackFlow {
      val dialog = AlertDialog.Builder(activity)
        .setItems(source.map { resources.getString(it.text) }.toTypedArray()) { dialog, which ->
          sendBlocking(source[which])
          dialog.dismiss()
        }
        .setNegativeButton(resources.getString(R.string.dialog_cancel_button)) { dialog, _ ->
          cancel(CancellationException("Dialog is closed"))
          dialog.dismiss()
        }
        .create()

      dialog.show()

      awaitClose { dialog.dismiss() }
    }

}

private sealed class Permission(val id: String, val requestCode: Int) {

  object Camera : Permission(CAMERA, 2)

  object Gallery : Permission(WRITE_EXTERNAL_STORAGE, 3)

  fun assertGranted(activity: Activity): Boolean {
    if (activity.checkSelfPermission(id) != PERMISSION_GRANTED) {
      activity.requestPermissions(arrayOf(id), requestCode)

      return false
    }

    return true
  }
}
