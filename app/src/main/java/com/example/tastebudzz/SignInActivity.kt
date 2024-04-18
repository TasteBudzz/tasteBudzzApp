package com.example.tastebudzz

import android.app.Activity
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
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.tastebudzz.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
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
    private lateinit var googleSignIn: AppCompatButton
    private lateinit var googleSignInClient: GoogleSignInClient

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

        // sign in with google
        googleSignIn.setOnClickListener {
            Log.e("AUTH", "Attempt sign in with google")

            signInGoogle()
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
                           Log.e("AUTH", "signInWithEmail:success")
                            val user = auth.currentUser
                            //go to default view after log in
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                           Log.e("AUTH", "signInWithEmail:failure", task.exception)
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

    private fun signInGoogle() {
        Log.e("AUTH", "launch sign in with google")



        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent

        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.e("AUTH", "launched sign in with google")

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            Log.e("AUTH", "handle sign in with google")

            handleResults(task)
        }
    }

    private val googleSignInActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
               Log.e("TAG", "onActivityResult -: ${result.data!!.extras}")

                val accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = accountTask.getResult(ApiException::class.java)
                   Log.e("TAG", "onActivityResult +: $account")

//                    firebaseAuthWithGoogleAccount(account)
                } catch (e: ApiException) {
                   Log.e("TAG", "onActivityResult = : ${e.message}")
                }
            } else {
               Log.e("TAG", "onActivityResult :) ${result.data}")
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account : GoogleSignInAccount? = task.result
            Log.e("AUTH", "checking google sign in ")

            if (account != null) {
                Log.e("AUTH", "sign in with google successful")

                updateUI(account)
            }
        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
           if (it.isSuccessful) {
               val intent = Intent(this, HomeActivity::class.java)
               startActivity(intent)
           } else {
               Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

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