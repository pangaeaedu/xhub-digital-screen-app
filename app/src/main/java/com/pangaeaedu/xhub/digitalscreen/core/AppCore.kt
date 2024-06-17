package com.pangaeaedu.xhub.digitalscreen.core

import android.app.Application
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

object AppCore {

    lateinit var instance: Application
        private set

    fun init(application: Application) {
        instance = application
    }
}

fun AppCore.getStringById(@StringRes resId: Int, vararg formatArgs: Any?): String {
    return instance.getString(resId, *formatArgs)
}

fun AppCore.getQuantityString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any?): String {
    return if (formatArgs.isEmpty()) {
        instance.resources.getQuantityString(id, quantity, quantity)
    } else {
        instance.resources.getQuantityString(id, quantity, *formatArgs)
    }
}

fun AppCore.getDimensionPixelSize(@DimenRes dimenResId: Int): Int {
    return instance.resources.getDimensionPixelSize(dimenResId)
}

fun AppCore.getColorById(@ColorRes colorResId: Int): Int {
    return ContextCompat.getColor(instance, colorResId)
}