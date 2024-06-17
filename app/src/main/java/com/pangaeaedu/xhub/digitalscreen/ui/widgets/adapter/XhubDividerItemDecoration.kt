package com.pangaeaedu.xhub.digitalscreen.ui.widgets.adapter

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.pangaeaedu.xhub.digitalscreen.R

class XhubDividerItemDecoration : DividerItemDecoration {
    constructor(context: Context?, orientation: Int) : super(context, orientation) {
        if (context != null) {
            val drawable = ContextCompat.getDrawable(context, R.drawable.gray_horizontal_divider)
            drawable?.let { setDrawable(it) }
        }
    }

    constructor(context: Context?, orientation: Int, @DrawableRes dividerDrawable: Int) : super(context, orientation) {
        if (context != null) {
            val drawable = ContextCompat.getDrawable(context, if (dividerDrawable == 0) R.drawable.gray_horizontal_divider else dividerDrawable)
            drawable?.let { setDrawable(it) }
        }
    }
}