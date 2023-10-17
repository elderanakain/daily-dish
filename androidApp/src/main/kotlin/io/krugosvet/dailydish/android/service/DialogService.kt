package io.krugosvet.dailydish.android.service

import android.app.AlertDialog
import android.content.res.Resources
import io.krugosvet.dailydish.android.service.permission.Permission
import io.krugosvet.dailydish.android.ui.container.ContainerActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DialogService(
    private val activity: ContainerActivity,
    private val resources: Resources,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
) {

    fun showPermissionExplanationDialog(permission: Permission) {
        AlertDialog.Builder(activity)
            .setMessage(permission.explanation)
            .setNeutralButton(android.R.string.ok) { _, _ -> }
            .setCancelable(false)
            .create()
            .show()
    }
}
