package com.pangaeaedu.xhub.digitalscreen.util

import com.pangaeaedu.xhub.digitalscreen.core.LogUtil
import com.pangaeaedu.xhub.digitalscreen.core.ToastUtil


object ExceptionLogUtil {
    const val MODE_NONE = 0
    private const val MODE_TOAST = 1
    private const val MODE_THROW = 2

    var defaultMode: Int = MODE_TOAST

    @JvmStatic
    @JvmOverloads
    fun log(e: Throwable, mode: Int = defaultMode) {
        if (LogUtil.isDebugEnabled) {
            when (mode) {
                MODE_TOAST ->
                    ToastUtil.showLong("Toast for debugging only!\n${e.javaClass.simpleName}: ${e.message}")
                MODE_THROW ->
                    throw e
            }
            e.printStackTrace()
        } else {
//            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}
