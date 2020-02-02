package io.krugosvet.dailydish.android.utils.baseUi

import androidx.annotation.*
import com.google.android.material.snackbar.*

fun showLongSnackbar(baseActivity: BaseActivity?, @StringRes errorMessageId: Int) {
  val coordinatorLayout = baseActivity?.getParentCoordinatorLayout()
  if (coordinatorLayout != null) {
    Snackbar.make(coordinatorLayout, baseActivity.getString(errorMessageId),
      Snackbar.LENGTH_LONG).show()
  }
}
