package io.krugosvet.dailydish.android.service

import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import io.krugosvet.dailydish.android.screen.container.view.ContainerActivity

class SnackbarService(
  private val activity: ContainerActivity
) {

  fun show(@StringRes errorMessageId: Int) =
    Snackbar
      .make(
        activity.bindingComponent.binding.parentCoordinatorLayout,
        activity.getString(errorMessageId),
        Snackbar.LENGTH_LONG
      )
      .show()
}
