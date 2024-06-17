package com.pangaeaedu.xhub.digitalscreen.viewholder

import android.os.Handler
import android.os.Looper
import com.pangaeaedu.xhub.digitalscreen.MainActivityCallback
import com.pangaeaedu.xhub.digitalscreen.R
import com.pangaeaedu.xhub.digitalscreen.core.AppCore
import com.pangaeaedu.xhub.digitalscreen.core.getStringById
import com.pangaeaedu.xhub.digitalscreen.data.ExamSessionInfo
import com.pangaeaedu.xhub.digitalscreen.databinding.ItemExamBinding
import com.pangaeaedu.xhub.digitalscreen.ui.widgets.adapter.BaseViewHolder
import com.pangaeaedu.xhub.digitalscreen.util.DataTimeUtilities
import java.util.Date

class ExamListItemViewHolder(
    override val binding: ItemExamBinding
) : BaseViewHolder(binding) {
    private var endTime: Long = 0L
    private val mHandler = Handler(Looper.getMainLooper())

    fun bindData(examInfo: ExamSessionInfo) {
        when (examInfo.examOrder){
            0 -> binding.tvExamSession.text = AppCore.getStringById(R.string.current_exam)
            1 -> binding.tvExamSession.text = AppCore.getStringById(R.string.next_exam)
            else -> binding.tvExamSession.text = AppCore.getStringById(R.string.following_exam)
        }
        binding.tvCandidate.text = examInfo.candidate
        binding.tvSubject.text = examInfo.subject
        binding.tvStatus.text = examInfo.status

        mHandler.removeCallbacks(countDownRunner)
        if (0 == examInfo.examOrder) {
            endTime = examInfo.beginTime + examInfo.duration
            countDownRunner.run()
        } else {
            binding.tvRemainingTime.text = AppCore.getStringById(
                R.string.exam_start_by,
                DataTimeUtilities.getLocaleDateTimeString(Date(examInfo.beginTime))
            )
        }

        binding.root.setOnClickListener {
//            if ("Check In" == examInfo.status || "Waiting" == examInfo.status) {
            (binding.root.context as MainActivityCallback).onGoToVerifyFingerPrint(examInfo)
//            }
        }
    }

    private val countDownRunner = object : Runnable {
        override fun run() {
            if (endTime - System.currentTimeMillis() >= 0) {
                binding.tvRemainingTime.text = AppCore.getStringById(
                    R.string.exam_remaining,
                    DataTimeUtilities.formatTime(endTime - System.currentTimeMillis())
                )
                countDown()
            } else {
                mHandler.removeCallbacks(this)
            }
        }
    }

    private fun countDown() {
        mHandler.postDelayed(countDownRunner, 1000)
    }
}
