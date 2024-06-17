package com.pangaeaedu.xhub.digitalscreen.util

import android.graphics.Paint
import android.widget.TextView

object ViewUtil {
       fun setTextViewUnderLine(textView: TextView) {
        textView.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        textView.paint.isAntiAlias = true
    }
}
