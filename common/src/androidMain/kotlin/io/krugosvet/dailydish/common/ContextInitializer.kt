package io.krugosvet.dailydish.common

import android.content.Context
import androidx.startup.Initializer

internal lateinit var appContext: Context
    private set

public class ContextInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        appContext = context.applicationContext
        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
