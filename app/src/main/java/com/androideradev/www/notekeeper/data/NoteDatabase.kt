package com.androideradev.www.notekeeper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.androideradev.www.notekeeper.NoteInfo

@Database(entities = [NoteInfo::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        // First we need to increase the version in the Database Annotation
        // We write migration to preserve the exist data from lose when change the schema of database
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE  notes ADD COLUMN description TEXT DEFAULT 'Add Description' NOT NULL")
            }

        }

        private var noteDatabaseInstance: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase? {
            if (noteDatabaseInstance == null) {

                synchronized(NoteDatabase::class.java) {
                    if (noteDatabaseInstance == null) {
                        noteDatabaseInstance = Room.databaseBuilder<NoteDatabase>(
                            context.applicationContext,
                            NoteDatabase::class.java, "note_database"
                        )//.addMigrations(MIGRATION_1_2)
                            .build()
                    }
                }
            }
            return noteDatabaseInstance
        }
    }
}
