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
interface TodoDao {

    @Query("SELECT * FROM  todo_table")
    fun getall(): LiveData<List<TodoRecords>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotes(TodoRecords: TodoRecords)

    @Upsert
    suspend fun upsertNotes(TodoRecords: TodoRecords)

    @Delete
    suspend fun deleteNotes(TodoRecords: TodoRecords)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAll()
}