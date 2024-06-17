package com.pangaeaedu.xhub.digitalscreen.core

import android.os.Looper
import android.os.SystemClock
import android.util.Log
import com.pangaeaedu.xhub.digitalscreen.BuildConfig
import kotlin.math.max

/**
 * We need to set the [BuildConfig.LOG_LEVEL] to ensure that
 * the log message whose log level is larger than [BuildConfig.LOG_LEVEL]
 * can be printed in any environment (Debug / Release).
 *
 * P.S The value of [BuildConfig.LOG_LEVEL] is different in the Debug or Release environment.
 *     see the file - [build.gradle] for detail.
 */
object LogUtil {
    @JvmStatic
    var logLevel: Int = BuildConfig.LOG_LEVEL

    @JvmStatic
    val isDebugEnabled: Boolean get() = logLevel <= Log.DEBUG

    @JvmStatic
    fun d(lazyMessage: () -> String?) {
        log(Log.DEBUG, lazyMessage)
    }

    @JvmStatic
    fun i(lazyMessage: () -> String?) {
        log(Log.INFO, lazyMessage)
    }

    @JvmStatic
    fun w(lazyMessage: () -> String?) {
        log(Log.WARN, lazyMessage)
    }

    @JvmStatic
    fun e(lazyMessage: () -> String?) {
        log(Log.ERROR, lazyMessage)
    }

    @JvmStatic
    fun d(e: Throwable?) {
        log(Log.DEBUG, e = e)
    }

    @JvmStatic
    fun i(e: Throwable?) {
        log(Log.INFO, e = e)
    }

    @JvmStatic
    fun w(e: Throwable?) {
        log(Log.WARN, e = e)
    }

    @JvmStatic
    @JvmOverloads
    fun e(e: Throwable?, lazyMessage: (() -> String?)? = null) {
        log(Log.ERROR, lazyMessage, e)
    }

    @JvmStatic
    fun enableMainThreadLog() {
        var mainThreadStartTime = -1L
        Looper.getMainLooper().setMessageLogging {
            if (it.startsWith(">>>>>")) {
                mainThreadStartTime = SystemClock.uptimeMillis()
            } else if (it.startsWith("<<<<<")) {
                val time = SystemClock.uptimeMillis() - mainThreadStartTime
                // 1000 / 50 = 20+ frames
                if (time > 50) d {
                    "Using ${time}ms ${it.substring(6)}"
                }
            }
        }
    }

    private fun log(level: Int, lazyMessage: (() -> String?)? = null, e: Throwable? = null) {
        if (level >= logLevel) {
            var message = lazyMessage?.let { it() }.orEmpty()
            if (message.isEmpty()) {
                if (e == null) {
                    return
                } else {
                    message = "null"
                }
            }
            val (tag, lineNumber) = generateTag()
            val lineTag = when {
                lineNumber < 10 -> "__$lineNumber"
                lineNumber < 100 -> "_$lineNumber"
                else -> "$lineNumber"
            }
            while (message.length > 4000) {
                val subMessage = message.substring(0, 4000)
                message = message.substring(4000)
                when (level) {
                    Log.DEBUG -> {
                        Log.d(lineTag, "$tag: $subMessage")
                    }
                    Log.INFO -> {
                        Log.i(lineTag, "$tag: $subMessage")
                    }
                    Log.WARN -> {
                        Log.w(lineTag, "$tag: $subMessage")
                    }
                    Log.ERROR -> {
                        Log.e(lineTag, "$tag: $subMessage")
                    }
                }
            }
            when (level) {
                Log.DEBUG -> {
                    Log.d(lineTag, "$tag: $message", e)
                }
                Log.INFO -> {
                    Log.i(lineTag, "$tag: $message", e)
                }
                Log.WARN -> {
                    Log.w(lineTag, "$tag: $message", e)
                }
                Log.ERROR -> {
                    Log.e(lineTag, "$tag: $message", e)
                }
            }
        }
    }

    private val NO_LOG_LIST = setOf(Thread::class.java.name, LogUtil::class.java.name)

    internal fun generateTag(): Pair<String, Int> {
        val stackTrace = Thread.currentThread().stackTrace
        for (i in 2 until stackTrace.size) {
            val className = stackTrace[i].className
            val fileName = stackTrace[i].fileName
            val lineNumber = stackTrace[i].lineNumber
            if (className !in NO_LOG_LIST && !fileName.isNullOrEmpty()) {
                return Pair(fileName.substring(0, max(fileName.lastIndexOf('.'), 0)), lineNumber)
            }
        }
        return Pair(javaClass.simpleName, 0)
    }
}
