package io.krugosvet.dailydish.android.utils.baseUi

import android.app.*
import android.support.annotation.*

open class BaseDialogFragment: DialogFragment() {
  protected fun getDimension(@DimenRes id: Int) = resources.getDimension(id)
}
