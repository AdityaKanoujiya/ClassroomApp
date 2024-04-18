package com.example.classroomapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class NotesRecord(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "body") var body: String?,
    @ColumnInfo(name = "bgcolor") var bgcolor: String?,
    @ColumnInfo(name = "date") val date: String,
//    @ColumnInfo(name = "month") val month: String?,
    @ColumnInfo(name = "time") val time: String?
)
