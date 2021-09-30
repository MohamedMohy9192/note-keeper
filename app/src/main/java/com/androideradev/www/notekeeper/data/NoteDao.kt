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

    @Query("SELECT * FROM notes")
    fun getAllNotes() : LiveData<List<NoteInfo>>
}