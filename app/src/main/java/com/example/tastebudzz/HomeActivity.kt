package com.example.tastebudzz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.RestaurantListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    lateinit var signoutButton: ImageView
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

        replaceFragment((RestaurantListFragment()))
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_restaurant_search
        auth = Firebase.auth
        // views and buttons

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            if (it.itemId == R.id.action_recipe ) {
                    //replaceFragment(HomeFragment())
                } else if (it.itemId == R.id.action_restaurant_search )
               {
                    replaceFragment(RestaurantListFragment())
                } else if (it.itemId == R.id.action_reviews ) {
                   // replaceFragment(SettingFragment())
                }
            true
        }

        signoutButton = findViewById(R.id.signoutButton)
        signoutButton.setOnClickListener{
            auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }


    }

    private fun replaceFragment(switchFragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.restaurant_frame_layout, switchFragment)
        fragmentTransaction.commit()
    }
}