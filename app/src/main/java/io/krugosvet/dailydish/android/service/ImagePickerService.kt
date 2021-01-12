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
import io.krugosvet.dailydish.android.architecture.injection.activityInject
import io.krugosvet.dailydish.android.service.ImagePickerService.DialogSource.Action
import io.krugosvet.dailydish.android.service.ImagePickerService.DialogSource.Action.Remove
import io.krugosvet.dailydish.android.service.ImagePickerService.DialogSource.Action.Update
import io.krugosvet.dailydish.android.service.permission.Permission
import io.krugosvet.dailydish.android.ui.container.view.ContainerActivity
import kotlinx.coroutines.rx2.awaitFirst

class ImagePickerService(
  private val activity: ContainerActivity,
) {

  data class Source(
    val permission: Permission,
    val pickerSource: Sources,
    override val text: Int
  ) :
    IDialogSource

  interface IDialogSource {
    val text: Int
  }

  sealed class DialogSource(@StringRes override val text: Int) :
    IDialogSource {

    sealed class Action(text: Int) : DialogSource(text) {
      object Update : Action(R.string.update_picture)
      object Remove : Action(R.string.remove_picture)
    }
  }

  private val imageSources = listOf(
    Source(Permission.Camera, Sources.CAMERA, R.string.take_a_picture),
    Source(Permission.Gallery, Sources.GALLERY, R.string.pick_from_gallery)
  )

  private val dialogService: DialogService by activity.activityInject()

  @Suppress("BlockingMethodInNonBlockingContext")
  suspend fun showImagePicker(isImageEmpty: Boolean): ByteArray? {
    val imageUri = when {
      isImageEmpty -> openImageProviderPicker()
      else -> openActionDialog()
    }

    return when (imageUri) {
      Uri.EMPTY -> null
      else -> activity.contentResolver.openInputStream(imageUri)?.readBytes()
    }
  }

  private suspend fun openActionDialog(): Uri =
    when (dialogService.showImagePickerDialog(Action::class.sealedObjects.toList())) {
      Update -> openImageProviderPicker()
      Remove -> Uri.EMPTY
    }

  private suspend fun openImageProviderPicker(): Uri {
    val source = dialogService.showImagePickerDialog(imageSources)

    return when {
      !assertGranted(source.permission) -> Uri.EMPTY
      else -> RxImagePicker.with(activity)
        .requestImage(source.pickerSource)
        .awaitFirst()
    }

  }

  /**
   * @return flag if flow should continue
   */
  private suspend fun assertGranted(permission: Permission): Boolean = when {
    checkSelfPermission(activity, permission.id) == PERMISSION_GRANTED -> true
    shouldShowRequestPermissionRationale(activity, permission.id) -> {
      dialogService.showPermissionExplanationDialog(permission)
      false
    }
    else -> activity.requestPermission(permission)
  }

}
