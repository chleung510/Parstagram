package com.example.parstagram.fragments

import android.util.Log
import com.example.parstagram.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment : FeedFragment() {

    override fun queryPosts(){
        // Specify which class(in our database) to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // Find all post objects
        query.include(Post.KEY_USER) // to fetch user name associated with each post
        //Limits only returns posts form currently signed in user
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
        // Return posts in descending order, newer posts will appear first
        query.addDescendingOrder("createdAt")
        query.setLimit(20)
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null){
                    // something went wrong
                    Log.e(TAG, "Error fetching posts")
                } else {
                    if (posts != null) {
                        for (post in posts){
                            Log.i(TAG, "Post: " + post.getDescription() + "User: " + post.getUser()?.username)
                        }

                        allPosts.clear()
                        allPosts.addAll(posts)
                        // Now we call setRefreshing(false) to signal refresh has finished
                        swipeContainer.setRefreshing(false)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}