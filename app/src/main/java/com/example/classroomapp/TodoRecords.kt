package com.example.classroomapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class TodoRecords(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "Task") var task: String?,
    @ColumnInfo(name = "Checked") var checked: Boolean?,
//    @ColumnInfo(name = "bgcolor") val bgcolor: String?,
//    @ColumnInfo(name = "date") val date: String,
//    @ColumnInfo(name = "month") val month: String?,
//    @ColumnInfo(name = "time") val time: String?
)
