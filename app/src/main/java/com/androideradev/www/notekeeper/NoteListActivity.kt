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
import com.google.android.material.snackbar.Snackbar

class NoteListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    NoteRecyclerAdapter.OnNoteItemClickListener {

    private lateinit var binding: ActivityNoteListBinding
    private val tag = NoteActivity::class.java.simpleName

    private var navUserSelection: Int? = null

    private val linearLayoutManager by lazy { LinearLayoutManager(this) }

    //Don't create NoteRecyclerAdapter instance until i actually use the property the first time
    //That will delay the creation on the NoteRecyclerAdapter instance
    //Default use of property without lazy clause will cause runtime error because we cant access Context
    //before on create method and  any property get instantiate when the activity
    // class instance created and before the call to onCreate method
    private val noteRecyclerAdapter by lazy { NoteRecyclerAdapter(this, DataManager.notes, this) }

    private val maxRecentlyViewedNotes = 5
    private val recentlyViewedNotes = ArrayList<NoteInfo>(maxRecentlyViewedNotes)
    private val recentlyViewedNoteRecyclerAdapter by lazy {
        NoteRecyclerAdapter(this, recentlyViewedNotes, this)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarNoteList.toolbar)


        binding.appBarNoteList.fab.setOnClickListener {
            val startEditNoteActivity = Intent(this, NoteActivity::class.java)
            startActivity(startEditNoteActivity)
        }

        navUserSelection = savedInstanceState?.getInt(NAV_USER_SELECTION_ID) ?: R.id.nav_notes

        when (navUserSelection) {
            R.id.nav_notes -> displayNotes()
            R.id.nav_courses -> displayCourses()
            R.id.nav_recently_viewed -> displayRecentlyViewedNotes()

        }


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

    private fun displayRecentlyViewedNotes() {
        binding.appBarNoteList.contentNoteList.notesRecyclerView.layoutManager =
            linearLayoutManager
        binding.appBarNoteList.contentNoteList.notesRecyclerView.adapter =
            recentlyViewedNoteRecyclerAdapter
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
                navUserSelection = R.id.nav_notes
            }
            R.id.nav_courses -> {
                displayCourses()
                navUserSelection = R.id.nav_courses
            }
            R.id.nav_recently_viewed -> {
                displayRecentlyViewedNotes()
                navUserSelection = R.id.nav_recently_viewed

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
            R.id.nav_how_many -> {
                val message = getString(
                    R.string.how_many_nav_value,
                    DataManager.notes.size, DataManager.courses.size
                )
                Snackbar.make(
                    binding.appBarNoteList.contentNoteList.notesRecyclerView,
                    message,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(NAV_USER_SELECTION_ID, navUserSelection!!)


    }

    override fun onNoteItemClick(note: NoteInfo) {
        Log.d(tag, "Note Item Clicked")
        addToRecentlyViewedNotes(note)
    }

    fun addToRecentlyViewedNotes(note: NoteInfo) {
        // Check if selection is already in the list
        val existingIndex = recentlyViewedNotes.indexOf(note)
        if (existingIndex == -1) {
            // it isn't in the list...
            // Add new one to beginning of list and remove any beyond max we want to keep
            recentlyViewedNotes.add(0, note)
            for (index in recentlyViewedNotes.lastIndex downTo maxRecentlyViewedNotes)
                recentlyViewedNotes.removeAt(index)
        } else {
            // it is in the list...
            // Shift the ones above down the list and make it first member of the list
            for (index in (existingIndex - 1) downTo 0)
                recentlyViewedNotes[index + 1] = recentlyViewedNotes[index]
            recentlyViewedNotes[0] = note
        }
    }

}