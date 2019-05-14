package com.tenclouds.swipeablerecyclerviewcell.utils

import androidx.databinding.BindingAdapter
import com.tenclouds.swipeablerecyclerviewcell.metaball.RIGHT_VIEW_TO_DELETE
import com.tenclouds.swipeablerecyclerviewcell.swipereveal.SwipeRevealLayout
import com.tenclouds.swipeablerecyclerviewcell.swipereveal.interfaces.OnIconClickListener

@BindingAdapter(value = ["onRightIconClicked", "onLeftIconClicked"], requireAll = false)
fun setListeners(view: SwipeRevealLayout,
                 right: OnRightIconClicked?,
                 left: OnLeftIconClicked?) {
    if (right == null && left == null) {
        view.removeIconClickListener()
    } else {
        view.setOnIconClickListener(object : OnIconClickListener {
            override fun onLeftIconClick(swipe: SwipeRevealLayout) {
                left?.onLeftIconClicked(swipe)
            }

            override fun onRightIconClick(swipe: SwipeRevealLayout) {
                right?.onRightIconClicked(swipe)
            }
        }, RIGHT_VIEW_TO_DELETE)
    }
}

interface OnRightIconClicked {
    fun onRightIconClicked(swipe: SwipeRevealLayout)
}

interface OnLeftIconClicked {
    fun onLeftIconClicked(swipe: SwipeRevealLayout)
}

