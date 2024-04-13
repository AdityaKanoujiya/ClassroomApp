package com.example.classroomapp

data class PdfFile(val fileName: String, val downloadUrl: String) {
    constructor() : this(fileName = "", downloadUrl = "")
}

