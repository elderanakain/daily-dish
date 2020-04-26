package io.krugosvet.dailydish.android.utils.baseUi

import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import io.krugosvet.dailydish.android.architecture.ui.BaseActivity

fun showLongSnackbar(baseActivity: BaseActivity<*>?, @StringRes errorMessageId: Int) {
  val coordinatorLayout = baseActivity?.getParentCoordinatorLayout()

  if (coordinatorLayout != null) {
    Snackbar
      .make(coordinatorLayout, baseActivity.getString(errorMessageId), Snackbar.LENGTH_LONG)
      .show()
  }
}
