package com.example.parstagram

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

//Description: String
//Image: File
//User: User

// This class is a model class allowing us represent our data and
// that subclass ParseObject to allow for Parse persistence
@ParseClassName("Post") // To link this class to the database class named "Post"
class Post : ParseObject() { // Post class extends ParseObject
    fun getDescription() : String? {
        // getString is a method allowing us to get string attributes using our key
        return getString(KEY_DESCRIPTION)
    }

    fun setDescription(description: String) {
        // allows us to add a key-value pair to this object
        put(KEY_DESCRIPTION, description)
    }

    fun getImage(): ParseFile? {
        // getParseFile is a method allowing us to get File attributes using our key
        return getParseFile(KEY_IMAGE)
    }

    fun setImage(parsefile: ParseFile) {
        // allows us to add a key-value pair to this object
        put(KEY_IMAGE, parsefile)
    }

    fun getUser(): ParseUser? {
        // getUser is a method allowing us to get USer attributes using our key
        return getParseUser(KEY_USER)
    }

    fun setUser(user: ParseUser){
        // allows us to add a key-value pair to this object
        put(KEY_USER, user)
    }
    companion object{
        //Sets of keys associated with attribute names defined in the database
        const val KEY_DESCRIPTION = "description"
        const val KEY_IMAGE = "image"
        const val KEY_USER = "user"
    }
}