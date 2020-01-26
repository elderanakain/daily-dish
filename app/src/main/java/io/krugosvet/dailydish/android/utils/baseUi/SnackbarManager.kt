package io.krugosvet.dailydish.android.utils.baseUi

import android.support.annotation.*
import android.support.design.widget.*

fun showLongSnackbar(baseActivity: BaseActivity?, @StringRes errorMessageId: Int) {
  val coordinatorLayout = baseActivity?.getParentCoordinatorLayout()
  if (coordinatorLayout != null) {
    Snackbar.make(coordinatorLayout, baseActivity.getString(errorMessageId),
      Snackbar.LENGTH_LONG).show()
  }
}
