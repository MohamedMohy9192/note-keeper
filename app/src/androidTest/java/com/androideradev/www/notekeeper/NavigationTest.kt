package com.androideradev.www.notekeeper

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.*
import androidx.test.espresso.action.ViewActions.*
import org.junit.Rule
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @Rule
    @JvmField
    val noteListActivity = ActivityScenarioRule(NoteListActivity::class.java)

    @Test
    fun selectNoteAfterNavigationDrawerChange() {
        // Open NavigationDrawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        // Select courses from within NavigationView
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_courses))

        //At this point our RecyclerView display courses items

        // Index of course item we want to select
        val coursePosition = 0

        // Select an item within The RecyclerView by specify the item ViewHolder
        // which represent each item in a RecyclerView adapter
        onView(withId(R.id.notes_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<CourseRecyclerAdapter.ViewHolder>(
                coursePosition,
                click()
            )
        )

        // We are going now to display the list of notes
        // Open NavigationDrawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        // Select courses from within NavigationView
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_notes))
        //At this point our RecyclerView display notes items
        // Index of note item we want to select
        val notePosition = 0

        onView(withId(R.id.notes_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<NoteRecyclerAdapter.ViewHolder>(
                notePosition,
                click()
            )
        )
        //Now We need to verifies that the app behaves as expected
        // verifies when switch to display the list of notes and the user
        // makes a selection of one of those notes that the notes gets displayed
        // is the correct note

        val note = DataManager.notes[notePosition]

        onView(withId(R.id.coursesSpinner)).check(matches(withSpinnerText(note.course?.title)))
        onView(withId(R.id.noteTitleEditText)).check(matches(withText(note.title)))
        onView(withId(R.id.noteTextEditText)).check(matches(withText(note.text)))
    }





}