package com.androideradev.www.notekeeper.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androideradev.www.notekeeper.*

class NoteRecyclerAdapter(
    private val context: Context, private var notes: List<NoteInfo>,
    private val itemClickListener: OnNoteItemClickListener
) :
    RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>() {

    private val tag = this::class.java.simpleName
    private val layoutInflater = LayoutInflater.from(context)

    interface OnNoteItemClickListener {
        fun onNoteItemClick(note: NoteInfo)
        fun onNoteItemDelete(note: NoteInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.note_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.noteCourseNameTextView.text = note.course?.title
        holder.noteTitleTextView.text = note.title
        holder.currentPosition = position
        holder.currentNoteId = note.id
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun setNotesFromDatabase(notesFromDatabase: List<NoteInfo>) {
      //  notes.addAll(notesFromDatabase)
        notes = notesFromDatabase
       // notifyItemRangeInserted(notes.lastIndex,  notesFromDatabase.size)
        notifyDataSetChanged()

    }

    // A nested class marked as inner can access the members of its outer class.
    // Inner classes carry a reference to an object of an outer class:

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentPosition = -1
        var currentNoteId = -1

        val noteCourseNameTextView: TextView = itemView.findViewById(R.id.course_name_text_view)
        val noteTitleTextView: TextView = itemView.findViewById(R.id.note_title_text_view)
        val noteDeleteIcon: ImageView = itemView.findViewById(R.id.delete_note_image_view)

        init {

            itemView.setOnClickListener {
                // TODO PASS THE ID OF THE NOTE INSTEAD OF POSITION

                context.startActivity(
                    Intent(context, NoteActivity::class.java).putExtra(
                        NOTE_ID, currentNoteId
                    )
                )
                itemClickListener.onNoteItemClick(notes[currentPosition])
            }

            noteDeleteIcon.setOnClickListener {
                Log.i(tag, "Note Delete Icon has Clicked $currentPosition")
                // Delete the note from recent viewed if exist
                itemClickListener.onNoteItemDelete(notes[currentPosition])
              //  DataManager.notes.removeAt(currentPosition)

                //notifyItemRemoved(currentPosition)
               //notifyItemRangeChanged(currentPosition, notes.size)


            }
        }

    }
}