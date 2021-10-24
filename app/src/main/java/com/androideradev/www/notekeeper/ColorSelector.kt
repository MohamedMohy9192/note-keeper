package com.androideradev.www.notekeeper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout

class ColorSelector @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val colors = listOf(Color.RED, Color.BLUE, Color.GRAY)
    private var selectedColorIndex = 0

    init {
        orientation = HORIZONTAL

        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.color_selector, this)

        view.findViewById<View>(R.id.color_select_view)
            .setBackgroundColor(colors[selectedColorIndex])

        view.setOnClickListener {
            selectPreviousColor()
        }

        view.setOnClickListener {
            selectNextColor()
        }
    }

    private fun selectNextColor() {
        if (selectedColorIndex == colors.lastIndex){
            selectedColorIndex = 0
        }

        selectedColorIndex++
        colors[selectedColorIndex]
    }

    private fun selectPreviousColor() {
       if (selectedColorIndex == 0){
           selectedColorIndex = colors.lastIndex
       }
        selectedColorIndex--
        colors[selectedColorIndex]
    }
}