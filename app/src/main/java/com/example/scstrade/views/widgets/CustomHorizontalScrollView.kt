package com.example.scstrade.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView

class CustomHorizontalScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : HorizontalScrollView(context, attrs) {

    private var callback: ((Int) -> Unit)? = null

    fun setScrollChangedCallback(cb: (scrollX: Int) -> Unit) {
        callback = cb
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        callback?.invoke(l)
    }

}