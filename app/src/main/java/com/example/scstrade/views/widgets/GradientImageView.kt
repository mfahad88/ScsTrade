package com.example.scstrade.views.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class GradientImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Define gradient
        val shader = LinearGradient(
            0f, height.toFloat(),  // Gradient from top to bottom
            0f, 0f,
            intArrayOf(Color.BLACK, Color.TRANSPARENT),  // Start and end colors
            floatArrayOf(0.3f, 1f),  // Gradient stops
            Shader.TileMode.CLAMP
        )

        paint.shader = shader
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }
}