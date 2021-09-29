package com.androideradev.www.notekeeper.data

import androidx.room.Dao
import androidx.room.Insert
import com.androideradev.www.notekeeper.NoteInfo

@Dao
interface NoteDao {

    @Insert
    fun addNote(note: NoteInfo)
}