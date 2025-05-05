package com.example.scstrade.views.widgets

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpaceItemDecoration(private val spaceHeight: Int,private val color: Int) : RecyclerView.ItemDecoration() {
    private val paint = Paint().apply {
        this.color = this@VerticalSpaceItemDecoration.color
    }
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        if(parent.getChildAdapterPosition(view)<parent.adapter!!.itemCount-1){
            outRect.bottom = spaceHeight
        }
        // Optional: add top margin only for the first item
        /*if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = spaceHeight
        }*/
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            // Bottom spacing
            val top = child.bottom + params.bottomMargin
            val bottom = top + spaceHeight

            canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)

            // Optional: draw top spacing for the first item
            if (parent.getChildAdapterPosition(child) == 0) {
                val firstTop = child.top - params.topMargin - spaceHeight
                val firstBottom = child.top - params.topMargin
                canvas.drawRect(left.toFloat(), firstTop.toFloat(), right.toFloat(), firstBottom.toFloat(), paint)
            }
        }
    }
}