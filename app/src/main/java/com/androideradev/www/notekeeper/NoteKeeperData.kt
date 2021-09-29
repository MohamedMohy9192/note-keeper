package com.androideradev.www.notekeeper

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import java.util.*

data class CourseInfo(
    @ColumnInfo(name = "course_id") val courseId: String,
    @ColumnInfo(name = "course_title") val title: String
) {
    // String representation for CourseInfo instances
    override fun toString(): String {
        return this.title
    }
}

@Entity
data class NoteInfo(

    @Embedded var course: CourseInfo? = null,
    var title: String? = null,
    var text: String? = null,
    val id: String = UUID.randomUUID().toString(),
)