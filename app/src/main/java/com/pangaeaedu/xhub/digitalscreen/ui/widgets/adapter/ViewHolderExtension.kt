package com.pangaeaedu.xhub.digitalscreen.ui.widgets.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding


inline fun <reified Binding : ViewBinding> ViewGroup.createBinding(): Binding {
    val inflateMethod = Binding::class.java.getMethod("inflate",
            LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    return inflateMethod.invoke(null, LayoutInflater.from(context), this, false) as Binding
}