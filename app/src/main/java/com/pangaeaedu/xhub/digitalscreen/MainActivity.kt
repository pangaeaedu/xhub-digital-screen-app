package com.pangaeaedu.xhub.digitalscreen

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.pangaeaedu.xhub.digitalscreen.core.ToastUtil
import com.pangaeaedu.xhub.digitalscreen.data.ExamSessionInfo
import com.pangaeaedu.xhub.digitalscreen.extensions.replaceFragmentClearBackStack
import com.pangaeaedu.xhub.digitalscreen.fragment.ExamInfoFragment
import com.pangaeaedu.xhub.digitalscreen.fragment.ExamListFragment
import com.pangaeaedu.xhub.digitalscreen.fragment.PolicyFragment
import com.pangaeaedu.xhub.digitalscreen.fragment.VerifyFingerPrintFragment
import com.pangaeaedu.xhub.digitalscreen.util.DataTimeUtilities
import com.pangaeaedu.xhub.digitalscreen.util.StatusBarCompat
import com.pangaeaedu.xhub.digitalscreen.util.ViewUtil
import java.util.Date


class MainActivity : FragmentActivity(), MainActivityCallback {
    private val mHandler = Handler(Looper.getMainLooper())

    private lateinit var mtvCurTime: TextView
    private lateinit var mtvExit: TextView
    private var bNeedCountDownToHome = false
    private val AUTO_GOTO_HOME_TIME = 20 * 1000L

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val colorPrimary = getColor(R.color.white)
        StatusBarCompat.setStatusBarStyle(this, colorPrimary, true)

        mtvCurTime = findViewById(R.id.tvTime)
        mtvExit = findViewById(R.id.tvExit)
        ViewUtil.setTextViewUnderLine(mtvExit)
        mtvExit.setOnClickListener { onGotoHome() }

        countDownTimeRunner.run()
        onGotoHome()
    }

    private val countDownTimeRunner = Runnable {
        if (isFinishing) {
            return@Runnable
        }

        mtvCurTime.text =
            DataTimeUtilities.getLocaleDateTimeString(Date(System.currentTimeMillis()))
        countDown()
    }

    private val countDownToHomeRunner = Runnable {
        if (isFinishing) {
            return@Runnable
        }

        onGotoHome()
    }

    private fun countDown() {
        mHandler.postDelayed(countDownTimeRunner, 1000)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        checkToGoHome()
        return super.dispatchTouchEvent(ev)
    }

    private fun checkToGoHome() {
        mHandler.removeCallbacks(countDownToHomeRunner)
        if (bNeedCountDownToHome) {
            mHandler.postDelayed(countDownToHomeRunner, AUTO_GOTO_HOME_TIME)
        }
    }

    override fun onGoToVerifyFingerPrint(examInfo: ExamSessionInfo) {
        replaceFragment(VerifyFingerPrintFragment.newInstance(examInfo))
    }

    override fun onVerifyFingerPrintSuccess(examInfo: ExamSessionInfo) {
        replaceFragment(PolicyFragment.newInstance(examInfo))
    }

    override fun onGotoExamInfoFragment(examInfo: ExamSessionInfo) {
        replaceFragment(ExamInfoFragment.newInstance(examInfo))
    }

    override fun onGotoHome() {
        replaceFragment(ExamListFragment.newInstance())
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment is ExamListFragment) {
            bNeedCountDownToHome = false
            mtvExit.visibility = View.GONE
            mHandler.removeCallbacks(countDownToHomeRunner)
        } else {
            bNeedCountDownToHome = true
            mtvExit.visibility = View.VISIBLE
            checkToGoHome()
        }
        replaceFragmentClearBackStack(R.id.contentFrame, fragment)
    }

    override fun callForHelp() {
        ToastUtil.showLong(R.string.help_is_called)
        onGotoHome()
    }
}
