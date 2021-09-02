package com.androideradev.www.notekeeper

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteRecyclerAdapter(private val context: Context, private val notes: List<NoteInfo>) :
    RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.note_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.noteCourseNameTextView.text = note.course?.title
        holder.noteTitleTextView.text = note.title
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    // A nested class marked as inner can access the members of its outer class.
    // Inner classes carry a reference to an object of an outer class:

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val noteCourseNameTextView: TextView = itemView.findViewById(R.id.course_name_text_view)
        val noteTitleTextView: TextView = itemView.findViewById(R.id.note_title_text_view)

        init {
            itemView.setOnClickListener {
                context.startActivity(
                    Intent(context, NoteActivity::class.java).putExtra(
                        NOTE_POSITION, adapterPosition
                    )
                )
            }
        }

    }
}