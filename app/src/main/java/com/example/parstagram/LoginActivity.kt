package com.example.parstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseObject
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Check if there's a user logged in
        // If there is, take them to MainActivity.
        if(ParseUser.getCurrentUser() != null) {
            goToMainActivity()
        }

        findViewById<Button>(R.id.login_button).setOnClickListener{
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            loginUser(username, password)
        }

        findViewById<Button>(R.id.signupBtn).setOnClickListener{
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            signUpUser(username, password)
        }

    }

    private fun signUpUser (username:String, password:String) {
        // Create the ParseUser
        val user = ParseUser()

        // Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)

        user.signUpInBackground { e ->
            if (e == null) { //if null pointer exception
                //User successfully logged in already


                goToMainActivity()

                Log.i(TAG, "Successfully signed up ")
            } else {

                e.printStackTrace()
                Toast.makeText(this, "Sign up unsuccessful, please try again!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // We use loginBackground because parse is a network call that can take long time
    // and we do not want to freeze our UI thread and instead we run it at the background
    private fun loginUser (username:String, password:String) {
        ParseUser.logInInBackground(username, password, ({ user, e ->
            if (user != null) {
                Log.i(TAG, "Login Successful")
                goToMainActivity()
            } else {
                e.printStackTrace() // for printing out the exception message
                Toast.makeText(this, "Error in logging in", Toast.LENGTH_SHORT).show()
            }})
        )
    }

    private fun goToMainActivity(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object{
        const val TAG = "LoginActivity"
    }
}
