package com.example.scstrade.views.widgets

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SideBarDivider (
    private val dividerHeight: Int = 1,          // In pixels
    private val dividerColor: Int = Color.GRAY,  // Color
    private val marginStart: Int = 0,           // In pixels
    private val marginEnd: Int = 0              // In pixels
) : RecyclerView.ItemDecoration() {

    private val paint = Paint().apply {
        color = dividerColor
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft + marginStart
        val right = parent.width - parent.paddingRight - marginEnd

        for (i in 0 until parent.childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + dividerHeight

            canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        }
    }

    // Optional: Add space for the divider
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position < state.itemCount - 1) {
            outRect.bottom = dividerHeight
        }
    }
}