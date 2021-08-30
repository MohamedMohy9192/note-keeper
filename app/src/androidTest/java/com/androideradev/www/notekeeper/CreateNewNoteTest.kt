package com.androideradev.www.notekeeper

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateNewNoteTest {

    @Rule
    @JvmField
    val noteListActivity: ActivityTestRule<NoteListActivity> = ActivityTestRule<NoteListActivity>(NoteListActivity::class.java)

    @Test
    fun createNewNote(){
        val noteTitle = "Test note title"
        val noteText = "This is the body of our test note"

        onView(withId(R.id.fab)).perform(click())

        onView(withId(R.id.noteTitleEditText)).perform(typeText(noteTitle))
        onView(withId(R.id.noteTextEditText)).perform(typeText(noteText))

    }
}