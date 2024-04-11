package com.example.tastebudzz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.RestaurantList
import com.example.tastebudzz.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RestaurantSearchActivity : AppCompatActivity() {
    lateinit var signoutButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_restaurant_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        replaceFragment((RestaurantList()))
        auth = Firebase.auth
        // views and buttons
        signoutButton = findViewById(R.id.signoutButton)

        signoutButton.setOnClickListener{
            auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }


    }

    private fun replaceFragment(restaurantListFragment: RestaurantList) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.restaurant_frame_layout, restaurantListFragment)
        fragmentTransaction.commit()
    }
}