package com.example.parstagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.parstagram.MainActivity
import com.example.parstagram.Post
import com.example.parstagram.PostAdapter
import com.example.parstagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

open class FeedFragment : Fragment() {

    lateinit var postsRecyclerView: RecyclerView

    lateinit var adapter : PostAdapter

    var allPosts: MutableList<Post> = mutableListOf() // empty list for populating items from server later

    lateinit var swipeContainer: SwipeRefreshLayout

    // To specify which layout file we want to use
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    //For initializing recyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postsRecyclerView = view.findViewById(R.id.postRecycleView)

        // Lookup the swipe container view
        swipeContainer = view.findViewById(R.id.swipeContainer)

        swipeContainer.setOnRefreshListener {
            // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            queryPosts()
        }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)

        // Set adapter on RecyclerView
        adapter = PostAdapter(requireContext(), allPosts as ArrayList<Post>)
        postsRecyclerView.adapter = adapter

        // Set layout manager on RecyclerView
        postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
    }

    // Query for all posts in our server
    open fun queryPosts() {
        // Specify which class(in our database) to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // Find all post objects
        query.include(Post.KEY_USER) // to fetch user name associated with each post

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

    companion object {
        const val TAG = "FeedFragment"
    }

}