package com.androideradev.www.notekeeper

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.androideradev.www.notekeeper.databinding.ActivityNoteListBinding

class NoteListActivity : AppCompatActivity() {


    private lateinit var binding: ActivityNoteListBinding
    private lateinit var arrayAdapter: ArrayAdapter<NoteInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)


        binding.fab.setOnClickListener {
            val startEditNoteActivity = Intent(this, MainActivity::class.java)
            startActivity(startEditNoteActivity)
        }

        val notesListView = binding.contentNoteList.notesListView
        arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            DataManager.notes
        )

        notesListView.adapter = arrayAdapter


        notesListView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val startEditNoteActivity = Intent(this, MainActivity::class.java)
                startEditNoteActivity.putExtra(NOTE_POSITION, position)
                startActivity(startEditNoteActivity)

            }
    }

    override fun onResume() {
        super.onResume()
        arrayAdapter.notifyDataSetChanged()
    }

}