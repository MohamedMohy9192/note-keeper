package com.androideradev.www.notekeeper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androideradev.www.notekeeper.CourseInfo
import com.androideradev.www.notekeeper.R
import com.google.android.material.snackbar.Snackbar

class CourseRecyclerAdapter(private val context: Context, private val courses: List<CourseInfo>) :
        RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_course_list, parent, false)
        return ViewHolder(itemView)
    }
    override fun getItemCount() = courses.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = courses[position]
        holder.textCourse.text = course.title
        holder.coursePosition = position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCourse: TextView = itemView.findViewById(R.id.course_title_text_view)
        var coursePosition = 0
        init {
            itemView.setOnClickListener {
                Snackbar.make(it, courses[coursePosition].title, Snackbar.LENGTH_LONG).show()
            }
        }
    }
}