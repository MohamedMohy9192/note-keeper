package com.androideradev.www.notekeeper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androideradev.www.notekeeper.databinding.ActivityNoteListBinding

class NoteListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)


        binding.fab.setOnClickListener {
            val startEditNoteActivity = Intent(this, NoteActivity::class.java)
            startActivity(startEditNoteActivity)
        }

        binding.contentNoteList.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.contentNoteList.notesRecyclerView.adapter = NoteRecyclerAdapter(this, DataManager.notes)
    }

    override fun onResume() {
        super.onResume()

        binding.contentNoteList.notesRecyclerView.adapter?.notifyDataSetChanged()
    }

}