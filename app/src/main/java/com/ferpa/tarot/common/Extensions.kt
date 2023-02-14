package com.ferpa.tarot.common

import android.content.Context
import android.view.View

object Extensions {


    /**
     * Extension function to get the width of the screen for a given [Context].
     *
     * @return the width of the screen in pixels
     */
    fun Context.getScreenWidth(): Int {
        val displayMetrics = this.resources.displayMetrics
        return displayMetrics.widthPixels
    }

    /**
     * Extension function to get the height of the screen for a given [Context].
     *
     * @return the height of the screen in pixels
     */
    fun Context.getScreenHeight(): Int {
        val displayMetrics = this.resources.displayMetrics
        return displayMetrics.heightPixels
    }

    /**
     * Extension function to set the size of a [View] as a card, based on the height of the screen.
     *
     * @param screenHeight the height of the screen in pixels
     */
    fun View.setCardSize(screenHeight: Int) {
        val height = screenHeight / 6
        val width = height / 2
        this.layoutParams.width = width
        this.layoutParams.height = height
    }

}