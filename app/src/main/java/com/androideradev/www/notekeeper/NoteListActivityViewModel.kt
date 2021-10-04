package com.androideradev.www.notekeeper

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import com.androideradev.www.notekeeper.data.NoteDao
import com.androideradev.www.notekeeper.data.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteListActivityViewModel(application: Application) : AndroidViewModel(application) {
    var navUserSelection: Int = R.id.nav_notes

    var newlyCreated = true

    private val maxRecentlyViewedNotes = 5
    val recentlyViewedNotes = ArrayList<NoteInfo>(maxRecentlyViewedNotes)

    var notesFromDatabase: LiveData<List<NoteInfo>> = MutableLiveData<List<NoteInfo>>()
    private var noteDao: NoteDao

    init {
        Log.i("NoteListViewModel", "Init Block Invoked")
        val noteDatabase = NoteDatabase.getDatabase(application)
        this.noteDao = noteDatabase!!.noteDao()
        notesFromDatabase = getAllNotes()

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

    fun saveState(outState: Bundle) {
        outState.putInt(NAV_USER_SELECTION_ID, navUserSelection)

        val notesIds = DataManager.noteIdsAsIntArray(recentlyViewedNotes)
        outState.putIntArray(RECENT_VIEWED_NOTES_IDS, notesIds)
    }

    fun restoreState(savedInstanceState: Bundle) {
        navUserSelection = savedInstanceState.getInt(NAV_USER_SELECTION_ID)
        val notesIds = savedInstanceState.getIntArray(RECENT_VIEWED_NOTES_IDS)
        val notes = DataManager.loadNotes(*notesIds!!)
        recentlyViewedNotes.addAll(notes)
    }

    private fun getAllNotes(): LiveData<List<NoteInfo>> {
        var notesFromDatabase: LiveData<List<NoteInfo>> = MutableLiveData<List<NoteInfo>>()
        viewModelScope.launch {
            notesFromDatabase = noteDao.getAllNotes()

        }
        return notesFromDatabase

    }

    fun deleteNote(note: NoteInfo) {
        viewModelScope.launch(Dispatchers.IO) {

            noteDao.deleteNote(note)

        }
    }

    fun updateNotes(notes: List<NoteInfo>) {
        viewModelScope.launch(Dispatchers.IO) {

            noteDao.updateNotes(notes)

        }
    }
    fun updateNote(vararg note: NoteInfo) {
        viewModelScope.launch(Dispatchers.IO) {

            noteDao.updateNote(*note)

        }
    }

}