package com.androideradev.www.notekeeper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.SeekBar
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat


class ColorSlider @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.seekBarStyle,
) : AppCompatSeekBar(context, attrs, defStyleAttr) {

    private var colors: ArrayList<Int> = arrayListOf(Color.RED, Color.YELLOW, Color.BLUE)

    private val tickMarksWidth = getPixelValueFromDP(16f)
    private val tickMarksHeight = getPixelValueFromDP(16f)
    private val halfTickMarksWidth = if (tickMarksWidth >= 0) tickMarksWidth / 2f else 1f
    private val halfTickMarksHeight = if (tickMarksHeight >= 0) tickMarksHeight / 2f else 1f
    private val paint = Paint()

    var drawableWidth = 0
    var drawableHeight = 0
    var halfDrawableWidth = 1
    var halfDrawableHeight = 1

    private var noColorDrawable: Drawable? = null
        set(value) {
            drawableWidth = value?.intrinsicWidth ?: 0
            drawableHeight = value?.intrinsicHeight ?: 0
            halfDrawableWidth = if (drawableWidth >= 0) drawableWidth / 2 else 1
            halfDrawableHeight = if (drawableHeight >= 0) drawableHeight / 2 else 1

            value?.setBounds(
                -halfDrawableWidth,
                -halfDrawableHeight,
                halfDrawableWidth,
                halfDrawableHeight
            )
            field = value
        }

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

        setPadding(
            paddingLeft,
            paddingTop,
            paddingRight,
            paddingBottom + getPixelValueFromDP(16f).toInt() // Padding has to be provided as an Int
        )
        thumb = AppCompatResources.getDrawable(context, R.drawable.ic_arrow_down_24)
        noColorDrawable =
            AppCompatResources.getDrawable(context, R.drawable.ic_baseline_clear_24)
        setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                listeners.forEach { function: (Int) -> Unit ->
                    function(colors[progress])
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    var selectedColorValue: Int = android.R.color.transparent
        set(value) {
            val index = colors.indexOf(value)
            progress = if (index == -1) {
                0
            } else {
                index
            }
        }

    private val listeners: ArrayList<(Int) -> Unit> = arrayListOf()
    fun addListener(function: (Int) -> Unit) {
        listeners.add(function)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawTickMarks(canvas)
    }

    private fun drawTickMarks(canvas: Canvas?) {
        canvas?.let {
            val count = colors.size
            val saveCount = canvas.save()
            canvas.translate(paddingLeft.toFloat(), (height / 2).toFloat() + getPixelValueFromDP(16f))

            val spacing = (width - paddingLeft - paddingRight) / (count - 1).toFloat()
            if (count > 1) {
                for (i in 0 until count) {
                    if (i == 0) {
                        noColorDrawable?.draw(canvas)
                    } else {
                        paint.color = colors[i]
                        canvas.drawRect(
                            -halfTickMarksWidth,
                            -halfTickMarksHeight,
                            halfTickMarksWidth,
                            halfTickMarksHeight,
                            paint
                        )
                    }
                    canvas.translate(spacing, 0f)
                }
                canvas.restoreToCount(saveCount)
            }
        }
    }

    // When we draw we need to work with pixels
    private fun getPixelValueFromDP(value: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            context.resources.displayMetrics // Use the current device's screen attributes for conversion
        )
    }

}