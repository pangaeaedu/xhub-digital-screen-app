package com.pangaeaedu.xhub.digitalscreen.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.pangaeaedu.xhub.digitalscreen.MainActivityCallback
import com.pangaeaedu.xhub.digitalscreen.R
import com.pangaeaedu.xhub.digitalscreen.constant.Key
import com.pangaeaedu.xhub.digitalscreen.core.ToastUtil
import com.pangaeaedu.xhub.digitalscreen.core.applyArguments
import com.pangaeaedu.xhub.digitalscreen.data.ExamSessionInfo
import com.pangaeaedu.xhub.digitalscreen.databinding.FragmentExamInfoBinding
import com.pangaeaedu.xhub.digitalscreen.ui.dialog.BaseAlertDialogFactory
import com.pangaeaedu.xhub.digitalscreen.util.DataTimeUtilities
import java.util.Date

class ExamInfoFragment : Fragment() {
    private lateinit var mBinding: FragmentExamInfoBinding
    private var mExamInfo: ExamSessionInfo? = null
    private var mDlg: AlertDialog? = null

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
        mBinding = FragmentExamInfoBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mExamInfo?.let { itExamInfo->
            mBinding.tvCandidateNo.text = itExamInfo.candidateId.toString()
            mBinding.tvCandidateName.text = itExamInfo.candidate
            mBinding.tvSubject.text = itExamInfo.subject

            mBinding.tvStartTime.text =
                DataTimeUtilities.getLocaleDateTimeStringWithoutYear(Date(itExamInfo.beginTime))
            mBinding.tvEndTime.text =
                DataTimeUtilities.getLocaleDateTimeStringWithoutYear(Date(itExamInfo.beginTime + itExamInfo.duration))

            mBinding.btnStorage.setOnClickListener {
                openStorage()
            }
        }
    }

    private val mHandler = Handler(Looper.getMainLooper())
    private fun openStorage() {
        mBinding.btnStorage.isEnabled = false
        mBinding.btnStorage.text = getString(R.string.storage_opened)
        ToastUtil.showShort(getString(R.string.storage_is_opened))
        mHandler.postDelayed(countDownRunner, 5000)
    }

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacks(countDownRunner)
        mDlg?.dismiss()
    }

    private val countDownRunner = Runnable {
        mBinding.btnStorage.isEnabled = true
        mBinding.btnStorage.text = getString(R.string.open_storage)
        ToastUtil.showShort(getString(R.string.storage_is_closed))
        showGuideToSecurityGate()
    }

    private fun showGuideToSecurityGate() {
        mDlg = BaseAlertDialogFactory.showOkDialog(
            requireActivity(),
            R.string.error_dialog_title,
            R.string.goto_security_gate_tip,
            null
        ) { _: DialogInterface? ->
            if(isAdded){
                (requireActivity() as MainActivityCallback).onGotoHome()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(examInfo: ExamSessionInfo) = ExamInfoFragment().applyArguments {
            putParcelable(Key.KEY_EXAM_INFO, examInfo)
        }
    }
}
