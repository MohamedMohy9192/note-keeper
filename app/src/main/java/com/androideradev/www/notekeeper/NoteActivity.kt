package com.androideradev.www.notekeeper

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.androideradev.www.notekeeper.databinding.ActivityNoteBinding
import com.androideradev.www.notekeeper.notifications.ReminderNotification
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : AppCompatActivity() {


    private val tag = NoteActivity::class.java.simpleName

    private var noteId = NOTE_ID_NOT_SET
    private var showLeftArrow = false

    private lateinit var binding: ActivityNoteBinding
    private lateinit var noteGetTogetherHelper: NoteGetTogetherHelper
    private lateinit var viewModel: NoteActivityViewModel

    lateinit var noteInfo: NoteInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        viewModel = ViewModelProvider(this)[NoteActivityViewModel::class.java]

        val coursesAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            DataManager.courses.values.toList()
        )

        coursesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.contentNote.coursesSpinner.adapter = coursesAdapter

        noteId =
            savedInstanceState?.getInt(NOTE_ID, NOTE_ID_NOT_SET) ?: intent.getIntExtra(
                NOTE_ID,
                NOTE_ID_NOT_SET
            )

        Log.i(tag, "Note notePosition $noteId")
        if (noteId != NOTE_ID_NOT_SET) {

            displayNote()
        } else {
            createNewNote()
        }


        ReminderNotification.createNotificationChannel(this)
        noteGetTogetherHelper = NoteGetTogetherHelper(this, lifecycle)



    }

    private fun createNewNote() {
        //Create new empty note and add it to the notes list in DataManager
        //Set the newly created note position to notePosition variable
        //  DataManager.notes.add(NoteInfo())

        // viewModel.insertNote(NoteInfo())
        //   Log.i(tag , "Note inserted")
        //Get the newly created note position which will be the last in item in the list
        //notePosition = DataManager.notes.lastIndex
        //  notePosition = viewModel.getLastNote()
        // Log.i(tag , "Last Note inserted ID $notePosition")

        //   saveNote()
    }

    private fun displayNote() {
        lifecycleScope.launch {
            noteInfo = viewModel.getNote(noteId)



            Log.i(tag, "Note $noteInfo")
            Log.i(tag, "Note Name ${noteInfo.id}")

            binding.contentNote.noteTitleEditText.setText(noteInfo.title)
            binding.contentNote.noteTextEditText.setText(noteInfo.text)

            val courseIndex = DataManager.courses.values.indexOf(noteInfo.course)
            binding.contentNote.coursesSpinner.setSelection(courseIndex)

            binding.contentNote.lastUpdatedTextView.text = formattedDate(noteInfo.date)
        }


    }


    private fun formattedDate(value: Date?): String {

        val simpleDateFormat = SimpleDateFormat("HH:mm d MMM, yyyy", Locale.getDefault())

        return "Last Updated: ${if (value == null) "NOT FOUND" else simpleDateFormat.format(value)}"
    }


/*
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
*/

    private fun saveNote() {
        // val note = DataManager.notes[notePosition]


        if (noteId != NOTE_ID_NOT_SET) {
            noteInfo.title = binding.contentNote.noteTitleEditText.text.toString()
            noteInfo.text = binding.contentNote.noteTextEditText.text.toString()

            noteInfo.course = binding.contentNote.coursesSpinner.selectedItem as CourseInfo

            val date = Calendar.getInstance().time
            noteInfo.date = date
            //displayNote()
            viewModel.updateNote(noteInfo)
        } else {
            val title = binding.contentNote.noteTitleEditText.text.toString()
            val text = binding.contentNote.noteTextEditText.text.toString()

            val course = binding.contentNote.coursesSpinner.selectedItem as CourseInfo
            val date = Calendar.getInstance().time
            val note = NoteInfo(course, title, text, date)
            // createNewNote()
            // viewModel.updateNote(note)
            viewModel.insertNote(note)

        }

    }


/*    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTE_POSITION, notePosition)
    }*/

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
                //  moveNext()
                return true
            }
            R.id.action_back -> {
                // moveBack()
                return true
            }
            R.id.action_get_together -> {
                //   noteGetTogetherHelper.sendMessage(DataManager.loadNote(notePosition))
                true
            }
            R.id.action_notification -> {
                //   ReminderNotification.notify(this, noteInfo.title!!, noteInfo.text!!, noteId)

                // Big Picture Style
                /*ReminderNotification.notify(
                    this,
                    noteInfo.title!!,
                    noteInfo.text!!,
                    R.drawable.gull_portrait_ca_usa,
                    noteId
                )*/

                // Big Text Style
                ReminderNotification.notify(
                    this,
                    "Collapsed Title",
                    "Collapsed Body Text",
                    R.drawable.ic_baseline_emoji_emotions_24,
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse tempor dolor et odio porta dictum. Ut ex urna, imperdiet sed.",
                    " Big Content Title",
                    " Summary Text",
                    noteId
                )

                // Inbox Style
                ReminderNotification.notify(this, noteId)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    /* override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
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
 */
    override fun onPause() {
        super.onPause()
        saveNote()
    }


}