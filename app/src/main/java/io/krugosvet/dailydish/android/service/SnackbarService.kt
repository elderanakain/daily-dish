package io.krugosvet.dailydish.android.service

import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import io.krugosvet.dailydish.android.ui.container.view.ContainerActivity

class SnackbarService(
  private val activity: ContainerActivity
) {

  fun show(@StringRes errorMessageId: Int) =
    Snackbar
      .make(
        activity.binding.parentCoordinatorLayout,
        activity.getString(errorMessageId),
        Snackbar.LENGTH_LONG
      )
      .show()
}
