package com.example.parstagram

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.parstagram.fragments.ComposeFragment
import com.example.parstagram.fragments.FeedFragment
import com.example.parstagram.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import java.io.File


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // For switching fragments into our FragmentView(container) based on tab we tap on
        val fragmentManager: FragmentManager = supportFragmentManager

        // this method returns boolean expression so we set it to true by default to express
        // we handled user's interaction on items.
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            item -> // creates a variable called item and passes into the function.

            var fragmentToShow : Fragment? = null
            // When an item associated with specific item id is clicked...
            when (item.itemId){
                R.id.action_home -> {
                    // To set fragmentToShow to FeedFragment
                    fragmentToShow = FeedFragment()
                }

                R.id.action_compose -> {
                    // To set fragmentToShow to ComposeFragment
                    fragmentToShow = ComposeFragment()
                }

                R.id.action_profile -> {
                    // To set fragmentToShow to ComposeFragment
                    fragmentToShow = ProfileFragment()
                }
            }

            if (fragmentToShow != null) {
                // To tell fragmentManager to replace container with fragment we want to show.
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
            }

            //return true to tell we have taken care of this user interaction  on the item
            true
        }

        // Set default selection
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_home




    fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout){
            ParseUser.logOut()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
    }
    companion object {
        const val TAG = "MainActivity"
    }
}

