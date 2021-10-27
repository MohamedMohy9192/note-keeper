package com.androideradev.www.notekeeper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat


class ColorSlider @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.seekBarStyle,
) : AppCompatSeekBar(context, attrs, defStyleAttr) {

    private var colors: ArrayList<Int> = arrayListOf(Color.RED, Color.YELLOW, Color.BLUE)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorSlider)
        try {
            colors = typedArray.getTextArray(R.styleable.ColorSlider_colors)
                .map {
                    Color.parseColor(it.toString())
                } as ArrayList<Int>
        } finally {
            typedArray.recycle()
        }
        colors.add(0, android.R.color.transparent)
        max = colors.size - 1
        progressBackgroundTintList =
            ContextCompat.getColorStateList(context, android.R.color.transparent)
        progressTintList = ContextCompat.getColorStateList(context, android.R.color.transparent)
        splitTrack = false

        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom + 50)
        thumb = AppCompatResources.getDrawable(context, R.drawable.ic_arrow_down_24)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            val count = colors.size
            val saveCount = canvas.save()
            canvas.translate(paddingLeft.toFloat(), (height / 2).toFloat() + 50f)
            if (count > 1) {
                for (i in 0 until count) {
                    val w = 48f
                    val h = 48f
                    val halfW = if (w >= 0) w / 2f else 1f
                    val halfH = if (h >= 0) h / 2f else 1f

                    val spacing = (width - paddingLeft - paddingRight) / (count - 1).toFloat()
                    val paint = Paint()
                    paint.color = colors[i]
                    canvas.drawRect(-halfW, -halfH, halfW, halfH, paint)
                    canvas.translate(spacing, 0f)
                }
                canvas.restoreToCount(saveCount)
            }
        }
    }

}