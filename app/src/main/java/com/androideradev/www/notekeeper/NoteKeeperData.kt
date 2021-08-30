package com.androideradev.www.notekeeper

data class CourseInfo(val courseId: String, val title: String) {
    // String representation for CourseInfo instances
    override fun toString(): String {
        return this.title
    }
}


data class NoteInfo(
    var course: CourseInfo? = null,
    var title: String? = null,
    var text: String? = null
)