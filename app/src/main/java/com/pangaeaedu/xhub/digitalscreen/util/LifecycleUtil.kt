package com.pangaeaedu.xhub.digitalscreen.util

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

object LifecycleUtil {
    fun runOnResume(activity: FragmentActivity, runnable: Runnable) {
        if (Looper.getMainLooper().thread !== Thread.currentThread()) {
            activity.runOnUiThread { runOnResume(activity, runnable) }
            return
        }
        val lifecycle = activity.lifecycle
        val checker = object : StateChecker {
            override fun isCorrect(): Boolean {
                return lifecycle.currentState == Lifecycle.State.RESUMED && !activity.supportFragmentManager.isStateSaved
            }
        }
        runNowOrNextResume(checker, lifecycle, runnable)
    }

    fun runOnResume(fragment: Fragment, runnable: Runnable) {
        if (Looper.getMainLooper().thread !== Thread.currentThread()) {
            Handler(Looper.getMainLooper()).post { runOnResume(fragment, runnable) }
            return
        }
        val lifecycle = fragment.lifecycle
        val checker = object : StateChecker {
            override fun isCorrect(): Boolean {
                return lifecycle.currentState == Lifecycle.State.RESUMED && !fragment.childFragmentManager.isStateSaved
            }
        }
        runNowOrNextResume(checker, lifecycle, runnable)
    }

    fun commitTransactionSafely(activity: FragmentActivity, transactionBuilder: TransactionBuilder?) {
        val lifecycle = activity.lifecycle
        val fragmentManager = activity.supportFragmentManager
        commitTransactionSafely(lifecycle, fragmentManager, transactionBuilder)
    }

    fun commitTransactionSafely(fragment: Fragment, transactionBuilder: TransactionBuilder?) {
        val lifecycle = fragment.lifecycle
        val fragmentManager = fragment.childFragmentManager
        commitTransactionSafely(lifecycle, fragmentManager, transactionBuilder)
    }

    /**
     * Transaction must be committed before onSaveInstanceState is called.
     */
    private fun commitTransactionSafely(lifecycle: Lifecycle, fragmentManager: FragmentManager,
                                        transactionBuilder: TransactionBuilder?) {
        val checker = object : StateChecker {
            override fun isCorrect(): Boolean {
                return !fragmentManager.isStateSaved && !fragmentManager.isDestroyed
            }
        }
        @SuppressLint("CommitTransaction") val runnable = Runnable { transactionBuilder?.createTransaction(fragmentManager.beginTransaction())?.commit() }
        runNowOrNextResume(checker, lifecycle, runnable)
    }

    private fun runNowOrNextResume(
        checker: StateChecker?,
        lifecycle: Lifecycle,
        runnable: Runnable) {
        if (checker == null || checker.isCorrect()) {
            runnable.run()
        } else {
            lifecycle.addObserver(LifecycleRunnableObserver(checker, lifecycle, runnable))
        }
    }

    interface TransactionBuilder {
        fun createTransaction(transaction: FragmentTransaction): FragmentTransaction
    }

    private interface StateChecker {
        fun isCorrect(): Boolean
    }

    private class LifecycleRunnableObserver constructor(
        private val checker: StateChecker?,
        private val lifecycle: Lifecycle,
        private val runnable: Runnable) : LifecycleObserver {
        private var errorStateCount = 0

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            if (checker != null && !checker.isCorrect()) {
                ExceptionLogUtil.log(IllegalStateException(
                        "Try to run on resume, but status is not correct! runnable:" + runnable
                                + ", count:" + ++errorStateCount))
            } else {
                try {
                    runnable.run()
                    lifecycle.removeObserver(this)
                } catch (e: Exception) {
                    ExceptionLogUtil.log(IllegalStateException(
                            "Try to run on resume, but an exception is thrown! runnable:$runnable", e))
                }
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            lifecycle.removeObserver(this)
        }

        override fun hashCode(): Int {
            return runnable.hashCode()
        }

        override fun equals(obj: Any?): Boolean {
            return if (obj is LifecycleRunnableObserver) {
                runnable == obj.runnable
            } else {
                false
            }
        }

    }
}