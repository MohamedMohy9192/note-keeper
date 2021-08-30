package com.androideradev.www.notekeeper

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.*
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CreateNewNoteTest {

    @Rule
    @JvmField
    val noteListActivity: ActivityTestRule<NoteListActivity> =
        ActivityTestRule<NoteListActivity>(NoteListActivity::class.java)

    @Test
    fun createNewNote() {
        val noteTitle = "Test note title"
        val noteText = "This is the body of our test note"

        // Specify the data item we wanna to select from the spinner
        val course = DataManager.courses["android_async"]

        // Open the MainActivity by taping on the floating action button
        onView(withId(R.id.fab)).perform(click())


        // Click on the spinner to open the drop down list to select an item
        onView(withId(R.id.coursesSpinner)).perform(click())
        // Select an item from the drop down list by specifying the type and the item object we are
        // looking for
        onData(allOf(instanceOf(CourseInfo::class.java), equalTo(course))).perform(click())

        onView(withId(R.id.noteTitleEditText)).perform(typeText(noteTitle))
        // Perform method can do more that one acton in this case we type the text then
        // close the soft keyboard
        onView(withId(R.id.noteTextEditText)).perform(typeText(noteText), closeSoftKeyboard())

        // we need to close the soft keyboard first se when we press the back button we exit the activity
        // and save the note
        pressBack()
        // Get the last note we just created
        val note = DataManager.notes.last()

        assertEquals(course, note.course)
        assertEquals(noteTitle, note.title)
        assertEquals(noteText, note.text)

    }
}