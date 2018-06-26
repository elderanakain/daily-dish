package io.krugosvet.dailydish.android.utils.baseUi

import android.support.annotation.StringRes
import android.support.design.widget.Snackbar

fun showLongSnackbar(baseActivity: BaseActivity?, @StringRes errorMessageId: Int) {
    val coordinatorLayout = baseActivity?.getParentCoordinatorLayout()
    if (coordinatorLayout != null) {
        Snackbar.make(coordinatorLayout, baseActivity.getString(errorMessageId),
                Snackbar.LENGTH_LONG).show()
    }
}
