package com.example.parstagram

import android.app.Application
import com.parse.Parse
import com.parse.ParseObject

// This application class is called to make connection to parse server when
// we first launch our app for our first time
class ParstagramApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // To register our application class with parse model(post)
        ParseObject.registerSubclass(Post::class.java)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build())
    }
}
