package com.androideradev.www.notekeeper

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import com.androideradev.www.notekeeper.data.NoteRepository

class NoteListActivityViewModel(application: Application) : AndroidViewModel(application) {
    var navUserSelection: Int = R.id.nav_notes

    var newlyCreated = true

    private val maxRecentlyViewedNotes = 5
    val recentlyViewedNotes = ArrayList<NoteInfo>(maxRecentlyViewedNotes)


    private val noteRepository = NoteRepository(application, viewModelScope)
    var notesFromDatabase: LiveData<List<NoteInfo>> = MutableLiveData<List<NoteInfo>>()



    init {
        Log.i("NoteListViewModel", "Init Block Invoked")
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
        return noteRepository.getAllNotes()
    }

    fun deleteNote(note: NoteInfo) {
        noteRepository.deleteNote(note)
    }

    fun updateNotes(notes: List<NoteInfo>) {
        noteRepository.updateNotes(notes)
    }
    fun updateNote(vararg note: NoteInfo) {
       noteRepository.updateNote(*note)
    }

}