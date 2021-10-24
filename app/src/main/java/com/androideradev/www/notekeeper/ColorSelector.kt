package com.androideradev.www.notekeeper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout

class ColorSelector @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val colors = listOf(Color.RED, Color.BLUE, Color.GREEN)
    private var selectedColorIndex = 0

    private var leftArrowImageView: ImageView
    private var rightArrowImageView: ImageView
    private var colorView: View

    init {
        orientation = HORIZONTAL

        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.color_selector, this)

        colorView = view.findViewById(R.id.color_select_view)
        colorView.setBackgroundColor(colors[selectedColorIndex])

        leftArrowImageView = view.findViewById(R.id.color_select_left_arrow)

        leftArrowImageView.setOnClickListener {
            selectPreviousColor()
        }

        rightArrowImageView = view.findViewById(R.id.color_select_right_arrow)

        rightArrowImageView.setOnClickListener {
            selectNextColor()
        }
    }

    private fun selectNextColor() {
        if (selectedColorIndex == colors.lastIndex) {
            selectedColorIndex = 0

        }else{
            selectedColorIndex++
        }
        colorView.setBackgroundColor(colors[selectedColorIndex])

    }

    private fun selectPreviousColor() {
        if (selectedColorIndex == 0) {
            selectedColorIndex = colors.lastIndex

        }else{
            selectedColorIndex--
        }

        colorView.setBackgroundColor(colors[selectedColorIndex])

    }
}