package io.krugosvet.dailydish.android.utils

import android.support.v4.app.DialogFragment

open class BaseDialogFragment: DialogFragment() {

    override fun dismiss() {
        super.dismiss()
        hideKeyboard(activity)
    }
}