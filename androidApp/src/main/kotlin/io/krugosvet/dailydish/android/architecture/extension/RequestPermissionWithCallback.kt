package io.krugosvet.dailydish.android.architecture.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions.Companion.EXTRA_PERMISSION_GRANT_RESULTS
import io.krugosvet.dailydish.android.architecture.extension.RequestPermissionWithCallback.Input
import io.krugosvet.dailydish.android.service.permission.Permission

class RequestPermissionWithCallback :
    ActivityResultContract<Input, Unit>() {

    class Input(
        val permission: Permission,
        val onPermissionResponse: (isGranted: Boolean) -> Unit
    )

    private lateinit var input: Input

    override fun createIntent(context: Context, input: Input): Intent {
        this.input = input

        return Intent(RequestMultiplePermissions.ACTION_REQUEST_PERMISSIONS)
            .putExtra(RequestMultiplePermissions.EXTRA_PERMISSIONS, arrayOf(input.permission.id))
    }

    override fun parseResult(resultCode: Int, intent: Intent?) {
        val isGranted = when {
            resultCode != Activity.RESULT_OK -> false
            else -> intent?.getIntArrayExtra(EXTRA_PERMISSION_GRANT_RESULTS)
                .run { isNotEmpty() && first() == PackageManager.PERMISSION_GRANTED }
        }

        input.onPermissionResponse(isGranted)
    }
}
