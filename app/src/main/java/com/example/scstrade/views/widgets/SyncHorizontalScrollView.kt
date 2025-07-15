package com.example.scstrade.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView

class SyncHorizontalScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : HorizontalScrollView(context, attrs) {

    companion object {
        private val scrollViews = mutableListOf<SyncHorizontalScrollView>()

        fun register(view: SyncHorizontalScrollView) {
            if (!scrollViews.contains(view)) scrollViews.add(view)
        }

        fun unregister(view: SyncHorizontalScrollView) {
            scrollViews.remove(view)
        }

        fun syncScroll(source: SyncHorizontalScrollView, scrollX: Int) {
            scrollViews.forEach {
                if (it != source) {
                    it.scrollTo(scrollX, 0)
                }
            }
        }
    }

    init {
        isHorizontalScrollBarEnabled = false
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        syncScroll(this, l)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        register(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unregister(this)
    }
}