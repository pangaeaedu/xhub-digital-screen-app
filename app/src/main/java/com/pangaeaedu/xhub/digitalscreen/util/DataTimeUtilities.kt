package com.pangaeaedu.xhub.digitalscreen.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DataTimeUtilities {

    /**
     * Transform the time in seconds in a string with format "H:M:SS".
     */
    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun formatTime(timeInMs: Long): String {
        val hours = (timeInMs / 1000L / 3600).toInt()
        val minutes = (timeInMs / 1000L / 60).toInt()
        val seconds = (timeInMs / 1000L % 60).toInt()
        return String.format("%2d:%2d:%02d", hours, minutes, seconds)
    }

    @JvmStatic
    fun getLocaleDateTimeString(date: Date?): String {
        if (date == null) {
            return ""
        }
        return SimpleDateFormat("MMMd yyyy HH:mm:ss", Locale.getDefault()).format(date)
    }

    @JvmStatic
    fun getLocaleDateTimeStringWithoutYear(date: Date?): String {
        if (date == null) {
            return ""
        }
        return SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault()).format(date)
    }
}
