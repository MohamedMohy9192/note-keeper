package com.androideradev.www.notekeeper

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

data class CourseInfo(
    @ColumnInfo(name = "course_id") val courseId: String,
    @ColumnInfo(name = "course_title") val title: String
) {
    // String representation for CourseInfo instances
    override fun toString(): String {
        return this.title
    }
}

@Entity(tableName = "notes")
data class NoteInfo(

    @Embedded var course: CourseInfo? = null,
    var title: String? = null,
    var text: String? = null,
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
)