package io.krugosvet.dailydish.android.service.permission

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import androidx.annotation.StringRes
import io.krugosvet.dailydish.android.R

sealed class Permission(
    val id: String,
    @StringRes val explanation: Int
) {

    object Camera : Permission(CAMERA, R.string.permission_explanation_camera)

    object Gallery : Permission(WRITE_EXTERNAL_STORAGE, R.string.permission_explanation_gallery)
}
