package com.androideradev.www.notekeeper.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androideradev.www.notekeeper.NoteInfo

@Dao
interface NoteDao {

    @Insert
    fun insertNote(note: NoteInfo)

    /*Room generates all the necessary code to update the LiveData object when a database is updated.
    The generated code runs the query asynchronously on a background thread when needed.*/
    @Query("SELECT * FROM notes")
    fun getAllNotes(): LiveData<List<NoteInfo>>


    /* An @Update method can optionally return an int value
     indicating the number of rows that were updated successfully.*/
    @Update
    fun updateNote(vararg note: NoteInfo): Int

    /*    @Query("SELECT * FROM notes WHERE id = :id")
        fun getNote(id: Int) : LiveData<NoteInfo>*/


    @Update
    fun updateNotes(notes: List<NoteInfo>)

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNote(id: Int): NoteInfo

    /* @Delete method can optionally return an int value
     indicating the number of rows that were deleted successfully.*/
    @Delete
    fun deleteNote(vararg note: NoteInfo): Int

}