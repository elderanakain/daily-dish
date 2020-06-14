package io.krugosvet.dailydish.core.service

import android.content.Context
import android.content.res.Resources
import androidx.annotation.StringRes

class ResourceService(context: Context) {

  private val resources: Resources = context.resources

  fun getString(@StringRes stringId: Int) = resources.getString(stringId)

  fun getString(@StringRes stringId: Int, argument: String) =
    resources.getString(stringId, argument)
}
