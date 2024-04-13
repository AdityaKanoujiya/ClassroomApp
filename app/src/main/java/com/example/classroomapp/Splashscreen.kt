package com.example.classroomapp

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.FirebaseApp

class Splashscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splashscreen)

var handler : Handler = Handler()

        handler.postDelayed({
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        },5500)

    }
}

class mainApp:Application(){
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}