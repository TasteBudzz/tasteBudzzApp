package com.example.tastebudzz

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.RestaurantListFragment
import com.Review
import com.SavedRecipesFragment
import com.example.tastebudzz.data.UserReviewsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception

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
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            if (it.itemId == R.id.action_recipe ) {
                Log.e("BOTTOM_NAV", "Selected your recipes screen")

                replaceFragment(SavedRecipesFragment())

            } else if (it.itemId == R.id.action_restaurant_search )
            {
                Log.e("BOTTOM_NAV", "Selected your restaurant screen")

                replaceFragment(RestaurantListFragment())
            } else if (it.itemId == R.id.action_reviews ) {
                Log.e("BOTTOM_NAV", "Selected your reviews screen")

                replaceFragment(UserReviewsFragment())

            }
            true
        }

        // navigate to correct fragment
            var dest = intent.getSerializableExtra("DEST")
            if (dest != null) {
                dest = dest as Int
                Log.e("BOTTOM_NAV", "Selected id ${dest}")

                if (dest == 1) {
                    Log.e("BOTTOM_NAV", "Selected your recipes screen")

                    findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_recipe
                } else if (dest == 2) {
                    Log.e("BOTTOM_NAV", "Selected your restaurant screen")

                    findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_restaurant_search

                } else if (dest == 3) {
                    Log.e("BOTTOM_NAV", "Selected your reviews screen")
                    findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_reviews

                }
            } else {
                findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_restaurant_search
            }

        auth = Firebase.auth
        // views and buttons
        if (findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId == R.id.action_restaurant_search){
            // ask for camera and write file permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                    val permission = arrayOf<String>(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    requestPermissions(permission, 112)
                }
            }
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