package com.example.parstagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.parse.FindCallback
import com.parse.ParseException

import com.parse.ParseQuery


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        queryPosts()
    }

    // Query for all posts in our server
    fun queryPosts() {
        // Specify which class(in our database) to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        query.getFirstInBackground(object : FindCallback<Post>{
            override fun done(objects: MutableList<Post>?, e: ParseException?) {

            }

        })
    }
    companion object {
        const val TAG = "MainActivity"
    }
}

