package com.androideradev.www.notekeeper

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.androideradev.www.notekeeper.data.NoteDao
import com.androideradev.www.notekeeper.data.NoteDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class InsertNoteTest {

    private lateinit var noteDao: NoteDao
    private lateinit var db: NoteDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, NoteDatabase::class.java
        ).build()
        noteDao = db.noteDao()
    }



    @Test
    @Throws(Exception::class)
    fun insertNewNote() {

        val noteTitle = "Test note title"
        val noteText = "This is the body of our test note"
        val course = DataManager.courses["android_async"]
        val note = NoteInfo(course, noteTitle, noteText)
        noteDao.insertNote(note)

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
       db.close()
    }

}