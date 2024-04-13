package com.example.classroomapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NotesRecord::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao

    companion object {
        @Volatile
        private var INSTANCE: NotesDatabase? = null

        fun getDatabase(context: Context): NotesDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized( this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    "notes_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()


                INSTANCE = instance
                return instance
            }
//            else {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    NotesDatabase::class.java,
//                    "notes_table"
//                ).build()
//
//                INSTANCE = instance
//                return instance
//            }
        }
    }
}
