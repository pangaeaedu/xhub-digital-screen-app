package com.pangaeaedu.xhub.digitalscreen

import android.app.Application
import com.pangaeaedu.xhub.digitalscreen.core.AppCore

/**
 *
 * &lt;br&gt;Created
 * @version
 *
 * @author   linsj
 *
 * @see
 *
 * Copyright(c) 2009-2014, TQ Digital Entertainment, All Rights Reserved
 *
 */
open class XHubApp : Application(){

    override fun onCreate() {
        super.onCreate()
        AppCore.init(this)
    }
}