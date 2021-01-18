package io.krugosvet.dailydish.android.service

import android.app.AlertDialog
import android.content.res.Resources
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.service.permission.Permission
import io.krugosvet.dailydish.android.ui.container.view.ContainerActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.concurrent.CancellationException
import kotlin.coroutines.resume

class DialogService(
  private val activity: ContainerActivity,
  private val resources: Resources,
  private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
) {

  fun showPermissionExplanationDialog(permission: Permission) {
    AlertDialog.Builder(activity)
      .setMessage(permission.explanation)
      .setNeutralButton(android.R.string.ok) { _, _ -> }
      .setCancelable(false)
      .create()
      .show()
  }

  suspend fun <T : ImagePickerService.IDialogSource> showImagePickerDialog(source: List<T>): T =
    withContext(mainDispatcher) {
      suspendCancellableCoroutine {
        val dialog = AlertDialog.Builder(activity)
          .setItems(source.map { resources.getString(it.text) }.toTypedArray()) { dialog, which ->
            it.resume(source[which])
            dialog.dismiss()
          }
          .setNegativeButton(resources.getString(R.string.dialog_cancel_button)) { dialog, _ ->
            it.cancel(CancellationException("Dialog is closed"))
            dialog.dismiss()
          }
          .create()

        dialog.show()

        it.invokeOnCancellation {
          dialog.dismiss()
        }
      }
    }
}
