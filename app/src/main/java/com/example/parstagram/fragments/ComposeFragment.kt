package com.example.parstagram.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.parstagram.MainActivity
import com.example.parstagram.Post
import com.example.parstagram.R
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.File


class ComposeFragment : Fragment() {

    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034 // A code for android to launch a new activity
    val photoFileName = "photo.jpg" // To give a name to the picture taken by camera
    var photoFile: File? = null // To define File type.

    lateinit var etDescription: EditText
    lateinit var ivPreview: ImageView

    // Telling us to use compose fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set onClickedListener and setup logic
        ivPreview = view.findViewById(R.id.imageView)

        //Fragment cannot call finViewById directly, so we need reference to view.
        view.findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            // send post to server without image
            // Get the description that user input
            val description = view.findViewById<EditText>(R.id.description).text.toString()
            val user = ParseUser.getCurrentUser() // get user we logged in

            if (photoFile != null){
                submitPost(description, user, photoFile!!) // to submit post with user and description
            } else {
                Log.e(MainActivity.TAG, "error in submitting post")
                Toast.makeText(requireContext(), "Photos must be taken before submitting a post", Toast.LENGTH_SHORT).show()
            }

        }

        view.findViewById<Button>(R.id.btnTakePicture).setOnClickListener {
            onLaunchCamera()
        }
    }

    // To send post parse object to server
    fun submitPost(description: String, user: ParseUser, file:File) {
        // Create post object
        val post = Post()

        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(file))
        post.saveInBackground { exception ->
            if (exception != null){
                Log.e(MainActivity.TAG, "Error in submitting the post")
                exception.printStackTrace()
                Toast.makeText(requireContext(), "Error in submitting the post", Toast.LENGTH_SHORT).show()
            } else {
                Log.i(MainActivity.TAG, "post successfully sent to server")
                //TODO: resetting description to be empty
                //TODO: resetting imageView to be empty
            }
        }
    }

    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE) // to let the user to choose specific app to take a picture
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName) // For saving the picture at specific location on the phone

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            // *** since we are in fragment file, we use requireContext() to get context.***
            val fileProvider: Uri =
                FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                // start the pictureCapActivity, take picture,
                // save it then return to main activity with request code.
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        // *** since we are in fragment file, we use requireContext() to get context.***
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MainActivity.TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(MainActivity.TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // When we complete the picCapActivity, saved our image and come back with request code that matches
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // by this point we have the camera photo on disk
                // We find that file and decode into bitmap
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                //Set out bitmap into our imageview in main activity
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(requireContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}