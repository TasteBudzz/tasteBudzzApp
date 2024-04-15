package com.example.tastebudzz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tastebudzz.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var emailText: EditText
    lateinit var passwordText: EditText
    lateinit var signinButton: Button
    lateinit var navSignup: TextView
    private lateinit var auth: FirebaseAuth
    private var oneTapClient: SignInClient? = null
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var googleSignIn: AppCompatButton
lateinit var progressBar: ProgressBar

    private val signInLauncher =  registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        this.onSignInResult(res)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            Log.v("AUTH", user.toString())
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } else {
            if (response == null) {
                Log.v("AUTH", result.toString())

                Toast.makeText(
                    baseContext,
                    "Authentication failed. Please try again.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
        Log.v("AUTH", FirebaseAuth.getInstance().currentUser.toString())

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        auth = Firebase.auth

        binding = ActivityMainBinding.inflate(layoutInflater)

        if (auth.currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // views and buttons
        emailText = findViewById(R.id.emailAddress)
        passwordText = findViewById(R.id.password)
        signinButton = findViewById(R.id.signinButton)
        navSignup = findViewById(R.id.navToSingup)
        progressBar = findViewById(R.id.progressBar)
        googleSignIn = findViewById(R.id.google_sign_in_button)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // sign in with google
        googleSignIn.setOnClickListener {
            oneTapClient = Identity.getSignInClient(this)
            signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.siva_default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build()
        }



        navSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        signinButton.setOnClickListener{
            // toogle sign in button and progress bar
            signinButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE



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
            // toogle sign in button and progress bar
            signinButton.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }

    }

    fun signInGoogle(view: View) {
        CoroutineScope(Dispatchers.Main).launch {
            signInGoogle()
        }
    }

    private suspend fun signInGoogle() {
        val result = oneTapClient?.beginSignIn(signInRequest)?.await()
        val intentSenderRequest = IntentSenderRequest.Builder(result!!.pendingIntent).build()
        activityResultLauncher.launch(intentSenderRequest)
    }

    private val activityResultLauncher: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                try {
                    val cred = oneTapClient!!.getSignInCredentialFromIntent(result.data)
                    val idToken = cred.googleIdToken
                    if (idToken != null) {
                        val firebaseCred = GoogleAuthProvider.getCredential(idToken, null)
                        auth.signInWithCredential(firebaseCred).addOnCompleteListener{
                            if (it.isSuccessful) {

                                Toast.makeText(
                                    baseContext,
                                    "Authentication successful.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            } else {
                                Toast.makeText(
                                    baseContext,
                                    "Authentication failed. Please try again.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                    }
                } catch (e: ApiException) {
                    Toast.makeText(
                        baseContext,
                        "Authentication failed. Please try again.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    //e.printStackTrace()
                }
            }
        }
    public override fun onStart() {
        super.onStart()
        super.onStart()
        if (auth.currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}