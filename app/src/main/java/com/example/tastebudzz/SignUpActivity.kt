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
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var emailText: EditText
    lateinit var passwordText: EditText
    lateinit var confirmPasswordText: EditText
    lateinit var firstNameText: EditText
    lateinit var lastNameText: EditText
    lateinit var signupButton: Button
    lateinit var navSignin: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        if (auth.currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        //get text and fields
        emailText = findViewById(R.id.emailAddress)
        passwordText = findViewById(R.id.password)
        confirmPasswordText = findViewById(R.id.confirmPassword)
        firstNameText = findViewById(R.id.firstName)
        lastNameText = findViewById(R.id.lastName)
        signupButton = findViewById(R.id.signupButton)
        navSignin = findViewById(R.id.navToSingin)

        navSignin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        signupButton.setOnClickListener{
            //get field text
            var email = emailText.text.toString()
            var password = passwordText.text.toString()
            var confirmPassword = confirmPasswordText.text.toString()
            var firstName = firstNameText.text.toString()
            var lastName = lastNameText.text.toString()

            //validate fields
            var fieldsValid = true
            if (email.length == 0 || email.contains('@') != true || email.contains('.') != true) {
                fieldsValid = false
                Toast.makeText(
                    baseContext,
                    "Authentication failed. Invalid Email Address",
                    Toast.LENGTH_SHORT,
                ).show()
            } else if (password.equals(confirmPassword) == false) {
                fieldsValid = false
                Toast.makeText(
                    baseContext,
                    "Authentication failed. Passwords don't match.",
                    Toast.LENGTH_SHORT,
                ).show()
            }else if (password.length < 6) {
                fieldsValid = false
                Toast.makeText(
                    baseContext,
                    "Authentication failed. Password must a length of at least 6.",
                    Toast.LENGTH_SHORT,
                ).show()
            } else if (firstName.length == 0) {
                fieldsValid = false
                Toast.makeText(
                    baseContext,
                    "Authentication failed. First name is blank.",
                    Toast.LENGTH_SHORT,
                ).show()
            } else if (lastName.length == 0) {
                fieldsValid = false
                Toast.makeText(
                    baseContext,
                    "Authentication failed. Last name is blank.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
            if (fieldsValid) {

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AUTH", "createUserWithEmail:success")
                            val user = auth.currentUser
                            val updateUser = UserProfileChangeRequest.Builder()
                                .setDisplayName(firstName + lastName)
                                .build()
                            user?.updateProfile(updateUser)
                                ?.addOnCompleteListener{ task ->
                                    if (task.isSuccessful) {
                                        Log.d("AUTH", "updateProfile:success")
                                    } else {
                                        Log.d("AUTH", "updateProfile:failure")

                                    }
                                }

                            //go to default view after log in
                            var intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AUTH", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            //updateUI(null)
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