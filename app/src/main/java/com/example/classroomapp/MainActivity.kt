package com.example.classroomapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.classroomapp.databinding.FragmentTodoBinding

class MainActivity : AppCompatActivity() {
    val TodoFragment: FragmentTodoBinding
        get() {
            TODO()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var intent =Intent(this, LoginPage::class.java)
        startActivity(intent)

//        val fragment = LoginForm()
//        val transaction = supportFragmentManager.beginTransaction()
//
//        // Replace the existing fragment with the new one
//        transaction.replace(R.id.screen, fragment)
//        transaction.addToBackStack(null) // Optional: Add transaction to back stack
//
//        transaction.commit()
    }
}