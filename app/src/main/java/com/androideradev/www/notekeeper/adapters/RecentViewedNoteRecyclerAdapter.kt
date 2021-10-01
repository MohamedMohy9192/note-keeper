package com.androideradev.www.notekeeper.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androideradev.www.notekeeper.NoteInfo
import com.androideradev.www.notekeeper.R

class RecentViewedNoteRecyclerAdapter(val context: Context, private val recentViewedNotes: List<NoteInfo>) :
    RecyclerView.Adapter<RecentViewedNoteRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(
            R.layout.recent_note_list_item, parent, false
        )

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recentNote = recentViewedNotes[position]
        holder.recentNoteCourseNameTextView.text = recentNote.course?.title
        holder.recentNoteTitleTextView.text = recentNote.title
    }

    override fun getItemCount(): Int {
        return recentViewedNotes.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val recentNoteCourseNameTextView: TextView =
            itemView.findViewById(R.id.recent_note_course_name_text_view)
        val recentNoteTitleTextView: TextView =
            itemView.findViewById(R.id.recent_note_title_text_view)


    }
}