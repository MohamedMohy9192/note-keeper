package com.androideradev.www.notekeeper.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.androideradev.www.notekeeper.NoteInfo

@Dao
interface NoteDao {

    @Insert
    fun insertNote(note: NoteInfo)

    /*Room generates all the necessary code to update the LiveData object when a database is updated.
    The generated code runs the query asynchronously on a background thread when needed.*/
    @Query("SELECT * FROM notes")
    fun getAllNotes() : LiveData<List<NoteInfo>>
}