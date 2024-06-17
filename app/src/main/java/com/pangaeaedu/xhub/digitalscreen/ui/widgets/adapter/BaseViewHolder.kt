package com.pangaeaedu.xhub.digitalscreen.ui.widgets.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


abstract class BaseViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    protected abstract val binding: ViewBinding

    init {
        binding.root.contentDescription = javaClass.simpleName
    }
}
