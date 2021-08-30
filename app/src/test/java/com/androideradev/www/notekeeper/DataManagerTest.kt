package com.androideradev.www.notekeeper

import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class DataManagerTest {

    @Before
    fun setUp() {
        //Clear the notes list before each test
        DataManager.notes.clear()
        //reinitialize the notes state with the same data before each test
        DataManager.initializeNotes()
    }

    @Test
    fun addNote() {
        val course = DataManager.courses["android_intents"]!!

        val noteTitle = "New Note Title"
        val noteText = "User Text For New Note Title"

        val newNoteIndex = DataManager.addNote(course, noteTitle, noteText)

        val note = DataManager.notes[newNoteIndex]

        assertEquals(NoteInfo(course, noteTitle, noteText), note)
    }

    @Test
    fun findSimilarNote() {
        //if we run the addNote Test and findSimilarNote together findSimilarNote will failed
        //because we in the both methods we add a note with the same values
        // so when we run all the tests in the class, addNote test will run first then findSimilar
        // so when we get the index of the note we added in this test it will back the index of the note we added in the
        //first test because findNote will return the first note in the notes list equal to the note arguments we pass to it
        // so we need to clean up the notes list before we run each test so we gonna use @Before
        val course = DataManager.courses["android_intents"]!!

        val noteTitle = "New Note Title"
        val noteText = "User Text For New Note Title"

        val newNoteIndex = DataManager.addNote(course, noteTitle, noteText)
        val note = DataManager.findNote(course, noteTitle, noteText)

        val findNoteIndex = DataManager.notes.indexOf(note)

        assertEquals(newNoteIndex, findNoteIndex)


    }
}