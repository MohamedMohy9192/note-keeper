package com.androideradev.www.notekeeper

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.androideradev.www.notekeeper.data.NoteDao
import com.androideradev.www.notekeeper.data.NoteDatabase
import kotlinx.coroutines.*

class NoteActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var noteDao: NoteDao

    private val job: Job = Job()
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + job)

    init {
        val noteDatabase = NoteDatabase.getDatabase(application)
        this.noteDao = noteDatabase!!.noteDao()
    }


    fun insertNote(note: NoteInfo) {

        viewModelScope.launch(Dispatchers.IO) {
            noteDao.insertNote(
                note
            )
        }

    }

    fun updateNote(vararg note: NoteInfo) {
        viewModelScope.launch(Dispatchers.IO) {

            noteDao.updateNote(*note)

        }
    }

    /*   fun getNote(id: Int): LiveData<NoteInfo> {
           var note: LiveData<NoteInfo> = MutableLiveData()

           viewModelScope.launch {
               note = noteDao.getNote(id)
           }

           return note
       }*/

    suspend fun getNote(id: Int): NoteInfo {


        val result = viewModelScope.async(Dispatchers.IO) {


            return@async noteDao.getNote(id)

        }



        return result.await()
    }

    fun deleteNote(note: NoteInfo) {
        viewModelScope.launch(Dispatchers.IO) {

            noteDao.deleteNote(note)

        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}