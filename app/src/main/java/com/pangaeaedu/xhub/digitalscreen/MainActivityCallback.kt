package com.pangaeaedu.xhub.digitalscreen

import com.pangaeaedu.xhub.digitalscreen.data.ExamSessionInfo


interface MainActivityCallback {
    fun onGoToVerifyFingerPrint(examInfo: ExamSessionInfo)
    fun onVerifyFingerPrintSuccess(examInfo: ExamSessionInfo)
    fun onGotoExamInfoFragment(examInfo: ExamSessionInfo)
    fun onGotoHome()
    fun callForHelp()
}