package com.example.classroomapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import javax.annotation.Nullable


class LoginPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var googleSignInClient: GoogleSignInClient? = null
    var req = 1

//    val sharedPreferences: SharedPreferences =
//        getSharedPreferences("PrefsFile", Context.MODE_PRIVATE)
//    val editor: SharedPreferences.Editor = sharedPreferences.edit()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        var signInWithGoogle: Button = findViewById(R.id.signInWithGoogle)
        var signInWithEmail : Button= findViewById(R.id.signInWithEmail)

        var email: EditText = findViewById(R.id.email)
        var pass : EditText = findViewById(R.id.password)


        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("190026049252-1lsit7cenrh8bgh7uhcti6eder0np6mu.apps.googleusercontent.com")
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);






        signInWithEmail.setOnClickListener(){
            var e = email.text.toString()
            var p = pass.text.toString()

            auth.signInWithEmailAndPassword(e,p)
                .addOnCompleteListener{
                    if(it.isSuccessful){

                    }
                    else{

                    }
                }
        }

        signInWithGoogle.setOnClickListener {
            val intent = googleSignInClient!!.signInIntent

            startActivityForResult(intent, 100)
        }

        auth = FirebaseAuth.getInstance()

        val firebaseUser: FirebaseUser? = auth.getCurrentUser()

        if (firebaseUser != null) {
            Toast.makeText(this,"user created",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@LoginPage, Homepage::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check condition
        if (requestCode == 100) {
            // When request code is equal to 100 initialize task
            val signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            // check condition
            if (signInAccountTask.isSuccessful) {
                // When google sign in successful initialize string
                val s = "Google sign in successful"

                // Display Toast
                displayToast("Google sign in successful")

                // Initialize sign in account
                try {
                    // Initialize sign in account
                    val googleSignInAccount = signInAccountTask.getResult(
                        ApiException::class.java
                    )
                    // Check condition
                    if (googleSignInAccount != null) {
//                        val displayName: String? = googleSignInAccount.displayName
//                        val photoUrl: String? = googleSignInAccount.photoUrl?.toString()
//
//                        editor.putString("userName", displayName)
//                        editor.putString("profileImg",photoUrl)
//                        editor.apply()


                        // When sign in account is not equal to null initialize auth credential
                        val authCredential =
                            GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
                        // Check credential
                        auth.signInWithCredential(authCredential)
                            .addOnCompleteListener(this,
                                OnCompleteListener<AuthResult?> { task ->
                                    // Check condition
                                    if (task.isSuccessful) {
                                        // When task is successful redirect to profile activity display Toast

                                        displayToast("Firebase authentication successful")
                                        startActivity(Intent(this@LoginPage, Homepage::class.java))
                                    } else {
                                        // When task is unsuccessful display Toast
                                        displayToast("Authentication Failed :" + task.exception!!.message)
                                    }
                                })
                    }
                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun displayToast(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()


    }

}