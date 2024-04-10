package com.example.tastebudzz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    lateinit var emailText: EditText
    lateinit var passwordText: EditText
    lateinit var signinButton: Button
    lateinit var navSignup: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        auth = Firebase.auth

        if (auth.currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // views and buttons
        emailText = findViewById(R.id.emailAddress)
        passwordText = findViewById(R.id.password)
        signinButton = findViewById(R.id.signinButton)
        navSignup = findViewById(R.id.navToSingup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        navSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        signinButton.setOnClickListener{
            //get fields
            var email = emailText.text.toString()
            var password = passwordText.text.toString()

            //validate fields
            var fieldsValid = true
            if (email.length == 0) {
                fieldsValid = false
                Toast.makeText(
                    baseContext,
                    "Authentication failed. Email field is empty.",
                    Toast.LENGTH_SHORT,
                ).show()
            } else if (email.contains('@') != true || email.contains('.') != true) {
                fieldsValid = false
                Toast.makeText(
                    baseContext,
                    "Authentication failed. Invalid Email Address",
                    Toast.LENGTH_SHORT,
                ).show()
            } else if (password.length == 0) {
                fieldsValid = false
                Toast.makeText(
                    baseContext,
                    "Authentication failed. Password is empty.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
            if (fieldsValid) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AUTH", "signInWithEmail:success")
                            val user = auth.currentUser
                            //go to default view after log in
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AUTH", "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed. Invalid Password or Email.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
        }

    }



    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            //reload()
        }
    }
}