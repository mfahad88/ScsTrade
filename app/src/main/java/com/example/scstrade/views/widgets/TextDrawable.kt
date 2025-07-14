package com.example.scstrade.views.widgets


import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import kotlin.math.min

class TextDrawable(
    private val text: String,
    private val bgColor: Int
) : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        textSize = 18f * Resources.getSystem().displayMetrics.density
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    override fun draw(canvas: Canvas) {
        val bounds = bounds

        // Draw background circle
        val radius = min(bounds.width(), bounds.height()) / 2f
        val cx = bounds.centerX().toFloat()
        val cy = bounds.centerY().toFloat()

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = bgColor
            style = Paint.Style.FILL
        }
        canvas.drawCircle(cx, cy, radius, bgPaint)

        // Draw centered text
        val xPos = cx
        val yPos = cy - (paint.descent() + paint.ascent()) / 2
        canvas.drawText(text, xPos, yPos, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}
