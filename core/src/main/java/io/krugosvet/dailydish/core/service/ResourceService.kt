package io.krugosvet.dailydish.core.service

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

class ResourceService(context: Context) {

  private val resources: Resources = context.resources

  fun getString(@StringRes stringId: Int): String =
    resources.getString(stringId)

  fun getString(@StringRes stringId: Int, argument: String): String =
    resources.getString(stringId, argument)

  fun getDimension(@DimenRes dimenRes: Int): Int =
    resources.getDimensionPixelSize(dimenRes)

  fun createUri(@DrawableRes drawableRes: Int): Uri =
    Uri.Builder()
      .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
      .authority(resources.getResourcePackageName(drawableRes))
      .appendPath(resources.getResourceTypeName(drawableRes))
      .appendPath(resources.getResourceEntryName(drawableRes))
      .build()
}
