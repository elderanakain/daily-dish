package io.krugosvet.dailydish.android.service

import android.app.AlertDialog
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.view.GenericBaseActivity
import io.krugosvet.dailydish.android.service.permission.Permission
import io.krugosvet.dailydish.core.service.ResourceService
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.CancellationException

class DialogService(
  private val activity: GenericBaseActivity,
  private val resources: ResourceService,
) {

  fun showPermissionExplanationDialog(permission: Permission) {
    AlertDialog.Builder(activity)
      .setMessage(permission.explanation)
      .setNeutralButton(android.R.string.ok) { _, _ -> }
      .setCancelable(false)
      .create()
      .show()
  }

  fun <T : ImagePickerService.IDialogSource> showImagePickerDialog(source: List<T>): Flow<T> =
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
