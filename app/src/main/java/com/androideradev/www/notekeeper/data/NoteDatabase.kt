package com.androideradev.www.notekeeper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.androideradev.www.notekeeper.NoteInfo

@Database(entities = [NoteInfo::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao()

    companion object {
        private var noteDatabaseInstance: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase? {
            if (noteDatabaseInstance == null) {

                synchronized(NoteDatabase::class.java) {
                    if (noteDatabaseInstance == null) {
                        noteDatabaseInstance = Room.databaseBuilder<NoteDatabase>(
                            context.applicationContext,
                            NoteDatabase::class.java, "note_database"
                        ).build()
                    }
                }
            }
            return noteDatabaseInstance
        }
    }
}
