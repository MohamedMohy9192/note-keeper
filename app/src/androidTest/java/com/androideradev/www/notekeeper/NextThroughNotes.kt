package com.androideradev.www.notekeeper

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.*
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NextThroughNotes {

    @Rule
    @JvmField
    val noteListActivity = ActivityTestRule(NoteListActivity::class.java)

    @Test
    fun nextThroughNotes() {

        //Click on the first item in the notes list view
        onData(
            allOf(
                instanceOf(NoteInfo::class.java),
                equalTo(DataManager.notes[0])
            )
        ).perform(click())

        // Loop through each note in the notes list and verify that each item in the notes list
        // correspond to the item displayed on the screen
        for (index in 0..DataManager.notes.lastIndex) {
            // Get the note at the current index
            val note = DataManager.notes[index]

            onView(withId(R.id.coursesSpinner)).check(matches(withSpinnerText(note.course?.title)))
            // Check if the current displayed note title correspond to the  note title in the DataManager
            onView(withId(R.id.noteTitleEditText)).check(matches(withText(note.title)))
            onView(withId(R.id.noteTextEditText)).check(matches(withText(note.text)))

            // Display the next item in the list by clicking the next action button in the actionbar
            // first we need to check if its not the last note in the list to perform the
            // click action on the next button
            if (index != DataManager.notes.lastIndex) {
                onView(withId(R.id.action_next)).perform(click())
            }
        }

        //After we reach the last note in the list verify that the next button is disable
        onView(withId(R.id.action_next)).check(matches(not(isEnabled())))

    }
}