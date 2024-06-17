package com.pangaeaedu.xhub.digitalscreen.util

import android.os.Build
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object DeviceUtil {
    @JvmStatic
    fun isMIUI() = !getSystemProperty("ro.miui.ui.version.name").isNullOrEmpty()// noinspection JavaReflectionMemberAccess

    /**
     * @return if the phone system is Flyme, return true;
     * Otherwise, return false;
     */
    @JvmStatic
    fun isFlyme(): Boolean {
        return try {
            // noinspection JavaReflectionMemberAccess
            Build::class.java.getMethod("hasSmartBar")
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun getSystemProperty(propName: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return line
    }
}
