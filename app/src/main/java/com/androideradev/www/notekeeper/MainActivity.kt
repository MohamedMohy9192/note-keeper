package com.androideradev.www.notekeeper

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.androideradev.www.notekeeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private var notePosition = POSITION_NOT_SET
    private var showLeftArrow = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val coursesAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            DataManager.courses.values.toList()
        )

        coursesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.contentMain.coursesSpinner.adapter = coursesAdapter

        notePosition = savedInstanceState?.getInt(NOTE_POSITION, POSITION_NOT_SET) ?:
            intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET)


        if (notePosition != POSITION_NOT_SET) {

            displayNote()
        } else {
            createNewNote()
        }


    }

    private fun createNewNote() {
        //Create new empty note and add it to the notes list in DataManager
        //Set the newly created note position to notePosition variable
        DataManager.notes.add(NoteInfo())
        //Get the newly created note position which will be the last in item in the list
        notePosition = DataManager.notes.lastIndex
    }

    private fun displayNote() {
        val note = DataManager.notes[notePosition]

        binding.contentMain.noteTitleEditText.setText(note.title)
        binding.contentMain.noteTextEditText.setText(note.text)

        val courseIndex = DataManager.courses.values.indexOf(note.course)
        binding.contentMain.coursesSpinner.setSelection(courseIndex)

    }

    private fun moveNext() {
        notePosition++
        displayNote()
        // Declare that the options menu has changed, so should be recreated.
        // The onCreateOptionsMenu(Menu) method will be called the next time it needs to be displayed.
        invalidateOptionsMenu()
    }

    private fun moveBack() {
        notePosition--
        displayNote()
        // Declare that the options menu has changed, so should be recreated.
        // The onCreateOptionsMenu(Menu) method will be called the next time it needs to be displayed.
        invalidateOptionsMenu()
    }

    private fun saveNote() {
        val note = DataManager.notes[notePosition]

        note.title = binding.contentMain.noteTitleEditText.text.toString()
        note.text = binding.contentMain.noteTextEditText.text.toString()

        note.course = binding.contentMain.coursesSpinner.selectedItem as CourseInfo
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTE_POSITION, notePosition)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_next -> {
                //Increment notePosition then display the note
                moveNext()
                return true
            }
            R.id.action_back -> {
                moveBack()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val menuItemNext = menu?.findItem(R.id.action_next)
        val menuItemBack = menu?.findItem(R.id.action_back)

        //Todo("Handle configuration change")

        // If we trying to add new note hide the next note icon form the action bar
        if (notePosition == POSITION_NOT_SET) {
            menuItemNext?.isVisible = false
            menuItemBack?.isVisible = false
        }

        if (notePosition >= DataManager.notes.lastIndex) {
            menuItemNext?.isEnabled = false
            menuItemBack?.isEnabled = true
            showLeftArrow = true
        }

        if (notePosition < DataManager.notes.lastIndex && showLeftArrow) {
            menuItemNext?.isEnabled = true
            menuItemBack?.isEnabled = true

        }

        val firstIndex = 0
        if (notePosition == firstIndex) {
            menuItemBack?.isEnabled = false
            menuItemNext?.isEnabled = true

            showLeftArrow = false
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        super.onPause()
        saveNote()
    }


}