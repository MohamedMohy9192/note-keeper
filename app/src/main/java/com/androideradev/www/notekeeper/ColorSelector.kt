package com.androideradev.www.notekeeper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout

class ColorSelector @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val colors = listOf(Color.RED, Color.BLUE, Color.GREEN)
    private var selectedColorIndex = 0

    private var colorView: View
    private var colorStatusCheckBox: CheckBox

    // set color selector views based on each note
    var selectedColorValue = Color.TRANSPARENT
        set(value) {
            //Get the index of color
            var colorIndex = colors.indexOf(value)
            if (colorIndex == -1) {
                // No color selected
                colorStatusCheckBox.isChecked = false
                colorIndex = 0
            } else {
                colorStatusCheckBox.isChecked = true
            }

            selectedColorIndex = colorIndex
            //set the color on the view
            colorView.setBackgroundColor(colors[selectedColorIndex])
        }

    private var colorSelectorListeners: ArrayList<((Int) -> Unit)> = arrayListOf()

    init {
        orientation = HORIZONTAL

        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.color_selector, this)

        colorView = view.findViewById(R.id.color_select_view)
        colorStatusCheckBox = view.findViewById(R.id.color_select_status_checkbox)

        colorView.setBackgroundColor(colors[selectedColorIndex])

        view.findViewById<ImageView>(R.id.color_select_left_arrow)
            .setOnClickListener {
                selectPreviousColor()
            }

        view.findViewById<ImageView>(R.id.color_select_right_arrow)
            .setOnClickListener {
                selectNextColor()
            }

        colorStatusCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            changeColor()
        }
    }

    fun addListener(function: (Int) -> Unit) {
        this.colorSelectorListeners.add(function)
    }

    private fun selectNextColor() {
        if (selectedColorIndex == colors.lastIndex) {
            selectedColorIndex = 0

        } else {
            selectedColorIndex++
        }
        colorView.setBackgroundColor(colors[selectedColorIndex])
        changeColor()

    }

    private fun selectPreviousColor() {
        if (selectedColorIndex == 0) {
            selectedColorIndex = colors.lastIndex

        } else {
            selectedColorIndex--
        }

        colorView.setBackgroundColor(colors[selectedColorIndex])
        changeColor()

    }

    private fun changeColor() {
        val color = if (colorStatusCheckBox.isChecked) {
            colors[selectedColorIndex]
        } else {
            Color.TRANSPARENT
        }

        //this.colorSelectorListener(color)

        this.colorSelectorListeners.forEach { function ->
            function(color)
        }
    }
}