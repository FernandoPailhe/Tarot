package com.ferpa.tarot.common

import android.content.Context

object Extensions {

    fun Context.getScreenWidth(): Int {
        val displayMetrics = this.resources.displayMetrics
        return displayMetrics.widthPixels
    }

    fun Context.getScreenHeight(): Int {
        val displayMetrics = this.resources.displayMetrics
        return displayMetrics.heightPixels
    }


}