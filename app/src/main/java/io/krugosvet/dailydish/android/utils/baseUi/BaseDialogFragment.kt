package io.krugosvet.dailydish.android.utils.baseUi

import android.support.annotation.DimenRes
import android.app.DialogFragment

open class BaseDialogFragment: DialogFragment() {
    protected fun getDimension(@DimenRes id: Int) = resources.getDimension(id)
}
