package com.androideradev.www.notekeeper

import android.os.Bundle
import androidx.lifecycle.ViewModel

class NoteListActivityViewModel : ViewModel() {
    var navUserSelection: Int = R.id.nav_notes

    var newlyCreated = true

    private val maxRecentlyViewedNotes = 5
    val recentlyViewedNotes = ArrayList<NoteInfo>(maxRecentlyViewedNotes)

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

}