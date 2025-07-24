package com.example.scstrade.views.widgets

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class StickyHeaderItemDecoration(private val isHeader: (position: Int) -> Boolean) : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        if (childCount == 0) return

        val firstView = parent.getChildAt(0) ?: return
        val firstPosition = parent.getChildAdapterPosition(firstView)

        if (!isHeader(firstPosition)) return

        val headerView = firstView
        val saveCount = c.save()
        c.translate(0f, 0f)
        headerView.draw(c)
        c.restoreToCount(saveCount)
    }
}