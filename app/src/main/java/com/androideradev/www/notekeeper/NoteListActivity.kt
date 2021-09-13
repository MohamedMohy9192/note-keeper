package com.androideradev.www.notekeeper

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androideradev.www.notekeeper.databinding.ActivityNoteListBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import java.util.*

class NoteListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    NoteRecyclerAdapter.OnNoteItemClickListener {

    private lateinit var binding: ActivityNoteListBinding
    private val tag = NoteActivity::class.java.simpleName

    private val linearLayoutManager by lazy { LinearLayoutManager(this) }

    //Don't create NoteRecyclerAdapter instance until i actually use the property the first time
    //That will delay the creation on the NoteRecyclerAdapter instance
    //Default use of property without lazy clause will cause runtime error because we cant access Context
    //before on create method and  any property get instantiate when the activity
    // class instance created and before the call to onCreate method
    private val noteRecyclerAdapter by lazy { NoteRecyclerAdapter(this, DataManager.notes, this) }


    private val recentlyViewedNoteRecyclerAdapter by lazy {
        RecentViewedNoteRecyclerAdapter(this, viewModel.recentlyViewedNotes)

    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            targetViewHolder: RecyclerView.ViewHolder
        ): Boolean {
            // Position of the list item want to drag
            val startPosition = viewHolder.adapterPosition
            // Position of the list item we want to swap with the dragged item
            val toPosition = targetViewHolder.adapterPosition
            /**
             * Todo this feature effect the recent viewed notes recycler view as well
             * but we only swap the items positions in the notes list
             * need to figure a way to swap the recent viewed notes list when the user display
             * the recent viewed notes nav selection
             * */
            // First swap the items in the list
            Collections.swap(DataManager.notes, startPosition, toPosition)
            // Notify the adapter of the items change positions
            recyclerView.adapter?.notifyItemMoved(startPosition, toPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            TODO("Not yet implemented")
        }
    })

    private lateinit var viewModel: NoteListActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarNoteList.toolbar)


        binding.appBarNoteList.fab.setOnClickListener {
            val startEditNoteActivity = Intent(this, NoteActivity::class.java)
            startActivity(startEditNoteActivity)
        }
        // When the activity destroy and re-created due to configuration change
        // the activity receive the same instance of the viewModel that was created when the activity initially created
        viewModel = ViewModelProvider(this)[NoteListActivityViewModel::class.java]

        // We depend viewModel to restore nav display selection and recent viewed notes due to configuration change
        // We only want to use savedInstanceState when the viewModel destroyed which happened when
        // system destroy activity to reclaim resources
        if (viewModel.newlyCreated && savedInstanceState != null) {
            viewModel.restoreState(savedInstanceState)

        }

        viewModel.newlyCreated = false
        // Display the correct list based on the nav user selection stored in the viewModel
        handleDisplaySelection(viewModel.navUserSelection)

        binding.appBarNoteList.contentNoteList.notesRecyclerView.setHasFixedSize(true)

        // Attach the the touch helper to recycler view
        itemTouchHelper.attachToRecyclerView(binding.appBarNoteList.contentNoteList.notesRecyclerView)
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


    // We use onSaveInstanceState to store nav user display selection
    // and recentlyViewedNotes list in the scenarios where ViewModel is not enough
    // that's when  system-initiated process death to reclaim resource
    // in this case viewModel is also destroyed so we need to use
    // onSaveInstanceState which Survives system-initiated process death
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveState(outState)

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
            R.id.nav_notes,
            R.id.nav_courses,
            R.id.nav_recently_viewed -> {

                handleDisplaySelection(itemId = item.itemId)
                //Save the user nav display selection into the viewModel navUserSelection property
                viewModel.navUserSelection = item.itemId

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

    private fun handleDisplaySelection(itemId: Int) {
        when (itemId) {

            R.id.nav_notes -> {
                // Handle the camera action
                displayNotes()

            }
            R.id.nav_courses -> {
                displayCourses()

            }
            R.id.nav_recently_viewed -> {
                displayRecentlyViewedNotes()


            }
        }
    }

    override fun onNoteItemClick(note: NoteInfo) {
        Log.d(tag, "Note Item Clicked")
        viewModel.addToRecentlyViewedNotes(note)
    }

    override fun onNoteItemDelete(note: NoteInfo) {
        val isRemoved = viewModel.recentlyViewedNotes.remove(note)

        if (isRemoved) {
            Log.i(tag, "Recently Note Removed: $isRemoved")
        }

    }


}