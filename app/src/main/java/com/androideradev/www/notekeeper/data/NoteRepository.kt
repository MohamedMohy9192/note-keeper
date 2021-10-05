package com.androideradev.www.notekeeper.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androideradev.www.notekeeper.NoteInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NoteRepository(application: Application, private val coroutineScope: CoroutineScope) {

    private var noteDao: NoteDao

    init {
        val noteDatabase = AppDatabase.getDatabase(application)
        this.noteDao = noteDatabase!!.noteDao()
    }

    fun insertNote(note: NoteInfo) {
        coroutineScope.launch(Dispatchers.IO) {
            noteDao.insertNote(note)
        }
    }

    fun updateNote(vararg note: NoteInfo) {
        coroutineScope.launch(Dispatchers.IO) {
            noteDao.updateNote(*note)
        }
    }

    suspend fun getNote(id: Int): NoteInfo {
        val result = coroutineScope.async(Dispatchers.IO) {
            return@async noteDao.getNote(id)
        }
        return result.await()
    }

    fun deleteNote(note: NoteInfo) {
        coroutineScope.launch(Dispatchers.IO) {
            noteDao.deleteNote(note)
        }
    }

     fun getAllNotes(): LiveData<List<NoteInfo>> {
        var notesFromDatabase: LiveData<List<NoteInfo>> = MutableLiveData<List<NoteInfo>>()
        coroutineScope.launch {
            notesFromDatabase = noteDao.getAllNotes()

        }
        return notesFromDatabase

    }

    fun updateNotes(notes: List<NoteInfo>) {
        coroutineScope.launch(Dispatchers.IO) {

            noteDao.updateNotes(notes)

        }
    }

} /*   fun getNote(id: Int): LiveData<NoteInfo> {
           var note: LiveData<NoteInfo> = MutableLiveData()

           viewModelScope.launch {
               note = noteDao.getNote(id)
           }

           return note
       }*/