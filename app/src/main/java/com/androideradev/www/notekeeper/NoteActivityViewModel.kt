package com.androideradev.www.notekeeper

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.androideradev.www.notekeeper.data.NoteRepository

class NoteActivityViewModel(application: Application) : AndroidViewModel(application) {


    private val noteRepository = NoteRepository(application, viewModelScope)

    fun insertNote(note: NoteInfo) {
        noteRepository.insertNote(note)
    }

    fun updateNote(vararg note: NoteInfo) {
        noteRepository.updateNote(*note)
    }


    suspend fun getNote(id: Int): NoteInfo {
        return noteRepository.getNote(id)
    }

    fun deleteNote(note: NoteInfo) {
        noteRepository.deleteNote(note)
    }


}