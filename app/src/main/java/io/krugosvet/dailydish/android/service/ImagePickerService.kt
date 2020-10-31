package io.krugosvet.dailydish.android.service

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat.checkSelfPermission
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.extension.sealedObjects
import io.krugosvet.dailydish.android.service.ImagePickerService.DialogSource.Action
import io.krugosvet.dailydish.android.service.ImagePickerService.DialogSource.Action.Remove
import io.krugosvet.dailydish.android.service.ImagePickerService.DialogSource.Action.Update
import io.krugosvet.dailydish.android.service.permission.Permission
import io.krugosvet.dailydish.android.ui.container.view.ContainerActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.rx2.asFlow

class ImagePickerService(
  private val activity: ContainerActivity,
  private val dialogService: DialogService
) {

  data class Source(
    val permission: Permission,
    val pickerSource: Sources,
    override val text: Int
  ):
    IDialogSource

  interface IDialogSource {
    val text: Int
  }

  sealed class DialogSource(@StringRes override val text: Int):
    IDialogSource{

    sealed class Action(text: Int) : DialogSource(text) {
      object Update : Action(R.string.update_picture)
      object Remove : Action(R.string.remove_picture)
    }
  }

  private val imageSources = listOf(
    Source(Permission.Camera, Sources.CAMERA, R.string.take_a_picture),
    Source(Permission.Gallery, Sources.GALLERY, R.string.pick_from_gallery)
  )

  fun showImagePicker(isImageEmpty: Boolean): Flow<Uri> = when {
    isImageEmpty -> openImageProviderPicker()
    else -> openActionDialog()
  }

  private fun openActionDialog(): Flow<Uri> =
    dialogService.showImagePickerDialog(Action::class.sealedObjects.toList())
      .flatMapConcat { source ->
        when (source) {
          Update -> openImageProviderPicker()
          Remove -> flowOf(Uri.EMPTY)
        }
      }

  private fun openImageProviderPicker(): Flow<Uri> =
    dialogService.showImagePickerDialog(imageSources)
      .flatMapConcat { source ->
        assertGranted(source.permission)
          .filter { isGranted -> isGranted }
          .map { source }
      }
      .flatMapConcat {
        RxImagePicker.with(activity)
          .requestImage(it.pickerSource)
          .asFlow()
      }

  private fun assertGranted(permission: Permission): Flow<Boolean> =
    when {
      checkSelfPermission(activity, permission.id) == PERMISSION_GRANTED -> flowOf(true)
      shouldShowRequestPermissionRationale(activity, permission.id) -> {
        dialogService.showPermissionExplanationDialog(permission)
        flowOf(false)
      }
      else -> {
        activity.requestPermission(permission)
        activity.permissionsObservable
      }
    }

}
