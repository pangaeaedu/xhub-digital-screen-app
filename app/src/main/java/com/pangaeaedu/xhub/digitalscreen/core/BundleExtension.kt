package com.pangaeaedu.xhub.digitalscreen.core

import android.os.Bundle
import androidx.fragment.app.Fragment


fun <T : Fragment> T.applyArguments(block: Bundle.(T) -> Unit) = apply {
    val arguments = arguments ?: Bundle().apply { arguments = this }
    arguments.apply { block(this@applyArguments) }
}