package com.pangaeaedu.xhub.digitalscreen.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi

/**
 * for modifying the style of status bar
 */
object StatusBarCompat {
    private const val TAG_MARGIN_TOP_ADDED = "tag_margin_top_added"
    private const val TAG_STATUS_BAR_VIEW = "tag_status_bar_view"

    /**
     * get the height dimension of status bar
     *
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId != 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

    /**
     * change the style of status bar
     * P.S must call this method after setContentView
     * @param activity        the current page
     * @param color           an integer similar to OxFFFFFFFF
     * @param isFontDark      whether the font color is dark
     */
    fun setStatusBarStyle(activity: Activity, @ColorInt color: Int, isFontDark: Boolean) {
        setStatusBarColor(activity, color)
        setStatusBarFontColor(activity, isFontDark)
    }

    /**
     * set the font color of the status bar is dark or light
     *
     * @param dark whether the font color is dark
     */
    private fun setStatusBarFontColor(activity: Activity, dark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setAndroid23StatusBarFontIconDark(activity, dark)
        } else if (DeviceUtil.isMIUI()) {
            setMIUIStatusBarFontIconDark(activity, dark)
        } else if (DeviceUtil.isFlyme()) {
            setFlymeStatusBarFontIconDark(activity, dark)
        }
    }

    /**
     * set the status bar to translucent
     */
    private fun setStatusBarColor(activity: Activity, @ColorInt color: Int) {
        // when the system version is higher than 5.0
        val window = activity.window
        val contentView = window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        val childView = contentView.getChildAt(0)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // in order to show the status bar font, because we can not set the font color of versions which is below than 6.0
        window.statusBarColor =
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && color == Color.WHITE) Color.rgb(
                0xBA,
                0xBA,
                0xBA
            ) else color
        if (childView != null) {
            childView.fitsSystemWindows = true
        }
    }

    /**
     * use reserved order to remove is more quickly.
     */
    private fun removeStatusBarViewIfExist(activity: Activity) {
        val window = activity.window
        val decorView = window.decorView as ViewGroup
        val statusBarView = decorView.findViewWithTag<View>(TAG_STATUS_BAR_VIEW)
        if (statusBarView != null) {
            decorView.removeView(statusBarView)
        }
    }

    /**
     * The MIUI system provides API to modify the font color of the status bar to black for phone whose version is lower than Android M
     *
     *
     * set the font color of the status bar
     * when the status is bright,the font color is black,
     * which is white when the status bar is dark
     *
     * @param dark whether the font color of status is black
     */
    private fun setMIUIStatusBarFontIconDark(activity: Activity, dark: Boolean) {
        try {
            val window = activity.window
            val clazz: Class<*> = window.javaClass
            @SuppressLint("PrivateApi") val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            if (dark) {
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * The Flyme system provides API to modify the font color of the status bar to black for phone whose version is lower than Android M
     *
     *
     * set the font color of the status bar
     * when the status is bright,the font color is black,
     * which is white when the status bar is dark
     *
     * @param dark whether the font color of status is black
     */
    private fun setFlymeStatusBarFontIconDark(activity: Activity, dark: Boolean) {
        try {
            val window = activity.window
            val lp = window.attributes
            val darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            value = if (dark) {
                value or bit
            } else {
                value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            window.attributes = lp
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * @param dark whether the font color of status is black
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun setAndroid23StatusBarFontIconDark(activity: Activity, dark: Boolean) {
        if (dark) {
            activity.window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
    }

    fun setStatusBarVisibility(view: View, visible: Boolean) {
        val visibility: Int
        visibility = if (!visible) {
            (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else {
            View.VISIBLE
        }
        view.systemUiVisibility = visibility
    }
}
