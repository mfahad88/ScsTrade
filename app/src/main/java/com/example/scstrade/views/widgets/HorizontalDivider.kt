package com.example.scstrade.views.widgets

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.compose.ui.unit.Dp
import androidx.recyclerview.widget.RecyclerView

class HorizontalDivider(private val verticalSpaceHeight: Dp): RecyclerView.ItemDecoration() {

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(1)) {
//            outRect.top = verticalSpaceHeight.value.toInt()
            outRect.bottom = verticalSpaceHeight.value.toInt()
        }
    }
}