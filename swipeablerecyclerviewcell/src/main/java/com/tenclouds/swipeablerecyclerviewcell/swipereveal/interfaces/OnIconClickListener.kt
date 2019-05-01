package com.tenclouds.swipeablerecyclerviewcell.swipereveal.interfaces

import com.tenclouds.swipeablerecyclerviewcell.swipereveal.SwipeRevealLayout


interface OnIconClickListener {
    fun onLeftIconClick(swipe: SwipeRevealLayout)

    fun onRightIconClick(swipe: SwipeRevealLayout)
}