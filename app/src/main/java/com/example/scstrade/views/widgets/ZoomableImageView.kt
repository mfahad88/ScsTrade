package com.example.scstrade.views.widgets


import android.content.Context
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView

class ZoomImageView(context: Context, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    private var matrix = Matrix()
    private var scale = 1f
    private var lastX = 0f
    private var lastY = 0f
    private var posX = 0f
    private var posY = 0f

    private val scaleDetector: ScaleGestureDetector
    private val gestureDetector: GestureDetector

    init {
        scaleType = ScaleType.MATRIX
        scaleDetector = ScaleGestureDetector(context, ScaleListener())
        gestureDetector = GestureDetector(context, GestureListener())
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x - posX
                lastY = event.y - posY
            }
            MotionEvent.ACTION_MOVE -> {
                if (!scaleDetector.isInProgress) {
                    posX = event.x - lastX
                    posY = event.y - lastY
                    updateMatrix()
                }
            }
        }
        return true
    }

    private fun updateMatrix() {
        matrix.setScale(scale, scale)
        matrix.postTranslate(posX, posY)
        imageMatrix = matrix
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scale *= detector.scaleFactor
            scale = scale.coerceIn(0.5f, 3.0f) // Limit zoom levels

            updateMatrix()
            return true
        }
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            scale = if (scale > 1f) 1f else 2f // Double-tap zoom
            updateMatrix()
            return true
        }
    }
}
