package com.androideradev.www.notekeeper

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androideradev.www.notekeeper.databinding.ActivityNoteListBinding
import com.google.android.material.navigation.NavigationView

class NoteListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityNoteListBinding
    private val tag = NoteActivity::class.java.simpleName

    private val linearLayoutManager by lazy { LinearLayoutManager(this) }

    //Don't create NoteRecyclerAdapter instance until i actually use the property the first time
    //That will delay the creation on the NoteRecyclerAdapter instance
    //Default use of property without lazy clause will cause runtime error because we cant access Context
    //before on create method and  any property get instantiate when the activity
    // class instance created and before the call to onCreate method
    private val noteRecyclerAdapter by lazy { NoteRecyclerAdapter(this, DataManager.notes) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarNoteList.toolbar)


        binding.appBarNoteList.fab.setOnClickListener {
            val startEditNoteActivity = Intent(this, NoteActivity::class.java)
            startActivity(startEditNoteActivity)
        }

        displayNotes()

        binding.appBarNoteList.contentNoteList.notesRecyclerView.setHasFixedSize(true)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.appBarNoteList.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun displayNotes() {
        binding.appBarNoteList.contentNoteList.notesRecyclerView.layoutManager =
            linearLayoutManager
        binding.appBarNoteList.contentNoteList.notesRecyclerView.adapter =
            noteRecyclerAdapter
    }

    private fun displayCourses() {
        binding.appBarNoteList.contentNoteList.notesRecyclerView.layoutManager =
            GridLayoutManager(
                this,
                resources.getInteger(R.integer.courses_span_count),
                RecyclerView.VERTICAL,
                false
            )

        binding.appBarNoteList.contentNoteList.notesRecyclerView.adapter =
            CourseRecyclerAdapter(this, DataManager.courses.values.toList())
    }

    override fun onResume() {
        super.onResume()

        binding.appBarNoteList.contentNoteList.notesRecyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_notes -> {
                // Handle the camera action
                displayNotes()

            }
            R.id.nav_courses -> {
                displayCourses()
            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}