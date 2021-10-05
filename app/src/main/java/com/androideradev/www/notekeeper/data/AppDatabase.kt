package com.androideradev.www.notekeeper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.androideradev.www.notekeeper.NoteInfo

@Database(entities = [NoteInfo::class], version = 2)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        // First we need to increase the version in the Database Annotation
        // We write migration to preserve the exist data from lose when change the schema of database
        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE  notes ADD COLUMN description TEXT DEFAULT 'Add Description' NOT NULL")
            }

        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE  notes ADD COLUMN last_updated_time INTEGER DEFAULT NULL")
            }
        }

        private var appDatabaseInstance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (appDatabaseInstance == null) {

                synchronized(AppDatabase::class.java) {
                    if (appDatabaseInstance == null) {
                        appDatabaseInstance = Room.databaseBuilder<AppDatabase>(
                            context.applicationContext,
                            AppDatabase::class.java, "note_database"
                        ).addMigrations(MIGRATION_1_2)
                            .build()
                    }
                }
            }
            return appDatabaseInstance
        }
    }
}
