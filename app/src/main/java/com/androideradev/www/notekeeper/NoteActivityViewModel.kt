package com.androideradev.www.notekeeper

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.androideradev.www.notekeeper.data.NoteDao
import com.androideradev.www.notekeeper.data.NoteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NoteActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var noteDao: NoteDao

    private val job: Job = Job()
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + job)

    init {
        val noteDatabase = NoteDatabase.getDatabase(application)
        this.noteDao = noteDatabase!!.noteDao()
    }


    fun insertNote(note: NoteInfo) {

        coroutineScope.launch {
            noteDao.insertNote(
                note
            )
        }

    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}