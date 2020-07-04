package io.krugosvet.dailydish.android.service

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
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
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

const val REQUEST_PERMISSION_CAMERA = 2

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

  private var pickerDisposable: Disposable? = null

  private val isCameraPermissionGranted
    get() = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) ==
      PackageManager.PERMISSION_GRANTED

  init {
    activity.lifecycle.addObserver(object : DefaultLifecycleObserver {

      override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        pickerDisposable?.dispose()
      }
    })
  }

  fun showImagePicker(isImageEmpty: Boolean) =
    if (isImageEmpty) {
      openImageProviderChooser()
    } else {
      openActionDialog()
    }

  private fun openActionDialog(): Single<Uri> =
    Single.just(true)
      .observeOn(AndroidSchedulers.mainThread())
      .showDialog(Action::class.sealedObjects.toTypedArray())
      .flatMap {
        when (it) {
          Update -> openImageProviderChooser()
          Remove -> Single.just(Uri.EMPTY)
        }
      }

  private fun getImageFromCamera(): Single<Uri> =
    if (isCameraPermissionGranted) {
      RxImagePicker.with(activity).requestImage(Sources.CAMERA).firstOrError()
    } else {
      ActivityCompat.requestPermissions(
        activity, arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSION_CAMERA
      )

      activity.permissionsObservable
        .flatMap {
          if (isCameraPermissionGranted) {
            RxImagePicker.with(activity).requestImage(Sources.CAMERA)
          } else {
            Observable.error(IllegalStateException("Permission is not granted"))
          }
        }
        .firstOrError()
    }

  private fun openImageProviderChooser(): Single<Uri> =
    Single.just(true)
      .observeOn(AndroidSchedulers.mainThread())
      .showDialog(Source::class.sealedObjects.toTypedArray())
      .flatMap {
        when (it) {
          Source.Camera -> getImageFromCamera()
          Source.Gallery -> RxImagePicker.with(activity)
            .requestImage(Sources.GALLERY)
            .firstOrError()
        }
      }

  private fun <T : DialogSource> Single<*>.showDialog(source: Array<T>) =
    flatMap {
      Single.create<T> { emitter ->
        AlertDialog.Builder(activity)
          .setItems(source.map { resources.getString(it.text) }.toTypedArray()) { dialog, which ->
            emitter.onSuccess(source[which])
            dialog.dismiss()
          }
          .setNegativeButton(resources.getString(R.string.dialog_cancel_button)) { dialog, _ ->
            emitter.onError(IllegalStateException("Dialog is closed"))
            dialog.dismiss()
          }
          .create().show()
      }
    }

}
