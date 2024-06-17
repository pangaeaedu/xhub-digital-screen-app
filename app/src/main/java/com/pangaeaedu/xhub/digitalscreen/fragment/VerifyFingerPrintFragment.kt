package com.pangaeaedu.xhub.digitalscreen.fragment

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.pangaeaedu.xhub.digitalscreen.MainActivityCallback
import com.pangaeaedu.xhub.digitalscreen.R
import com.pangaeaedu.xhub.digitalscreen.constant.Key
import com.pangaeaedu.xhub.digitalscreen.core.AppCore
import com.pangaeaedu.xhub.digitalscreen.core.ToastUtil
import com.pangaeaedu.xhub.digitalscreen.core.applyArguments
import com.pangaeaedu.xhub.digitalscreen.core.getQuantityString
import com.pangaeaedu.xhub.digitalscreen.core.getStringById
import com.pangaeaedu.xhub.digitalscreen.data.ExamSessionInfo
import com.pangaeaedu.xhub.digitalscreen.databinding.FragmentVerifyFringerPrintBinding
import com.pangaeaedu.xhub.digitalscreen.ui.dialog.BaseAlertDialogFactory

class VerifyFingerPrintFragment : Fragment() {
    private lateinit var mBinding: FragmentVerifyFringerPrintBinding
    private var mVerifyFailedCount = 0
    private val MAX_VERIFY_FAILED_COUNT = 3
    private var mDlg: AlertDialog? = null

    private var mExamInfo: ExamSessionInfo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (savedInstanceState ?: arguments)?.apply {
            mExamInfo = getParcelable(Key.KEY_EXAM_INFO)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentVerifyFringerPrintBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        verifyFingerPrint()
    }

    private fun verifySuccess() {
        mExamInfo?.let {
            (requireActivity() as MainActivityCallback).onVerifyFingerPrintSuccess(it)
        } ?: {
            (requireActivity() as MainActivityCallback).onGotoHome()
        }
    }

    private fun verifyFailed() {
        mVerifyFailedCount++
        if (mVerifyFailedCount >= MAX_VERIFY_FAILED_COUNT) {
            mBinding.ivFingerPrint.isEnabled = false
            mDlg = BaseAlertDialogFactory.showConfirmActionDialog(
                requireActivity(),
                R.string.error_dialog_title,
                R.string.exceed_max_times,
                android.R.string.ok
            ) { _: DialogInterface?, i: Int ->
                when (i) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if (isAdded) {
                            (requireActivity() as MainActivityCallback).callForHelp()
                        }
                    }

                    DialogInterface.BUTTON_NEGATIVE -> {
                        if (isAdded) {
                            (requireActivity() as MainActivityCallback).onGotoHome()
                        }
                    }
                }
            }
        } else {
            ToastUtil.showLong(
                AppCore.getStringById(
                    R.string.verify_finger_print_failed,
                    AppCore.getQuantityString(R.plurals.times, mVerifyFailedCount, mVerifyFailedCount)
                )
            )
        }
    }

    override fun onPause() {
        super.onPause()
        mDlg?.dismiss()
    }

    private var fingerDownStamp: Long = 0
    private val VERIFY_TIME_BOUNDARY = 1500

    @SuppressLint("ClickableViewAccessibility")
    private fun verifyFingerPrint() {
        mBinding.ivFingerPrint.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    fingerDownStamp = System.currentTimeMillis()
                }

                MotionEvent.ACTION_UP -> {
                    if (System.currentTimeMillis() - fingerDownStamp < VERIFY_TIME_BOUNDARY) {
                        verifyFailed()
                    } else {
                        verifySuccess()
                    }
                }
            }
            true
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(examInfo: ExamSessionInfo) = VerifyFingerPrintFragment().applyArguments {
            putParcelable(Key.KEY_EXAM_INFO, examInfo)
        }
    }
}
