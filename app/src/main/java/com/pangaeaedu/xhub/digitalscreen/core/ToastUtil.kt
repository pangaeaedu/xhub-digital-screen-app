package com.pangaeaedu.xhub.digitalscreen.core

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.StringRes

object ToastUtil {
    /**
     * Shows a Toast message for a duration of [Toast.LENGTH_SHORT].
     *
     * @param stringResId the resource ID of the Toast message to display
     */
    @JvmStatic
    fun showShort(@StringRes stringResId: Int) {
        if (stringResId != 0) {
            showShort(AppCore.getStringById(stringResId))
        }
    }

    /**
     * Shows a Toast message for a duration of [Toast.LENGTH_SHORT].
     *
     * @param stringResId the resource ID of the Toast message to display
     * @param formatArgs the arguments to the string resource
     */
    fun showShort(@StringRes stringResId: Int, vararg formatArgs: Any?) {
        if (stringResId != 0) {
            showShort(AppCore.getStringById(stringResId, *formatArgs))
        }
    }

    /**
     * Shows a Toast message for a duration of [Toast.LENGTH_SHORT].
     *
     * @param message the Toast message to display
     */
    @JvmStatic
    fun showShort(message: CharSequence?) {
        showToast(message, Toast.LENGTH_SHORT)
    }

    /**
     * Shows a Toast message for a duration of [Toast.LENGTH_LONG].
     *
     * @param stringResId the resource ID of the Toast message to display
     */
    @JvmStatic
    fun showLong(@StringRes stringResId: Int) {
        if (stringResId != 0) {
            showLong(AppCore.getStringById(stringResId))
        }
    }

    /**
     * Shows a Toast message for a duration of [Toast.LENGTH_LONG].
     *
     * @param stringResId the resource ID of the Toast message to display
     * @param formatArgs the arguments to the string resource
     */
    fun showLong(@StringRes stringResId: Int, vararg formatArgs: Any?) {
        if (stringResId != 0) {
            showLong(AppCore.getStringById(stringResId, *formatArgs))
        }
    }

    /**
     * Shows a toast message for a duration of [Toast.LENGTH_LONG].
     *
     * @param message the message to display.
     */
    fun showLong(message: CharSequence) {
        showToast(message, Toast.LENGTH_LONG)
    }

    private fun showToast(message: CharSequence?, duration: Int) {
        if (message == null || message.isEmpty()) {
            return
        }
        Handler(Looper.getMainLooper()).post {
            val toast = Toast.makeText(AppCore.instance, message, duration)
            toast.show()
        }
    }
}