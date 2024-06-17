package com.pangaeaedu.xhub.digitalscreen.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExamSessionInfo(
    val id: Long,
    val examOrder: Int,
    val subject: String,
    val candidateId: Long,
    val candidate: String,
    val beginTime: Long,
    val duration: Long,
    val status: String
) : Parcelable
