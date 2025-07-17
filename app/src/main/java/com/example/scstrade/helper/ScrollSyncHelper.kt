package com.example.scstrade.helper

import com.example.scstrade.views.widgets.CustomHorizontalScrollView

object ScrollSyncHelper {
    private val scrollViews = mutableSetOf<CustomHorizontalScrollView>() // Use Set to avoid duplicates
    private var isSyncing = false
    private var lastScrollX = 0

    fun register(scrollView: CustomHorizontalScrollView) {
        if (scrollViews.add(scrollView)) { // Set prevents duplicates
            scrollView.scrollTo(lastScrollX, 0)

            scrollView.setScrollChangedCallback { scrollX ->
                if (isSyncing) return@setScrollChangedCallback

                isSyncing = true
                lastScrollX = scrollX

                scrollViews.forEach {
                    if (it != scrollView) {
                        it.scrollTo(scrollX, 0)
                    }
                }

                isSyncing = false
            }
        }
    }

    fun unregister(scrollView: CustomHorizontalScrollView) {
        scrollViews.remove(scrollView)
    }

    fun clear() {
        scrollViews.clear()
    }
}
