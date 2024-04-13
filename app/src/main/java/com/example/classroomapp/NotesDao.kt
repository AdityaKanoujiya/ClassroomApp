package com.example.classroomapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import java.util.concurrent.Flow

@Dao
interface NotesDao {

    @Query("SELECT * FROM  notes_table")
    fun getall(): LiveData<List<NotesRecord>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotes(NotesRecord: NotesRecord)

    @Upsert
    suspend fun upsertNotes(NotesRecord: NotesRecord)

    @Delete
    suspend fun deleteNotes(NotesRecord: NotesRecord)

    @Query("DELETE FROM notes_table")
    suspend fun deleteAll()
}