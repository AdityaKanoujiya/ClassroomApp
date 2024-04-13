package com.example.classroomapp

import javax.security.auth.Subject

import java.io.Serializable

data class ClassDataClass(
    val id: String,
    val batch: String,
    val subject: String,
    val teacherName: String
) : Serializable
