package com.example.scstrade.helper

import com.example.scstrade.views.widgets.CustomHorizontalScrollView

object ScrollSyncHelper {
    val scrollViews = mutableListOf<CustomHorizontalScrollView>()

    fun register(scrollView: CustomHorizontalScrollView) {
        if (!scrollViews.contains(scrollView)) {
            scrollViews.add(scrollView)
            scrollView.setScrollChangedCallback { scrollX ->
                scrollViews.forEach {
                    if (it != scrollView) it.scrollTo(scrollX, 0)
                }
            }
        }
    }
}
