package com.example.myphotostock

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_photo_from_gallery_preview.*
import kotlinx.android.synthetic.main.cell_photo.view.*
import java.lang.Exception

class PhotoFromGalleryPreviewActivity : AppCompatActivity() {

    internal lateinit var auth: FirebaseAuth
    private lateinit var userID: String

    internal lateinit var refDatabase: DatabaseReference
    private lateinit var refDbListOfPhotos: DatabaseReference

    private lateinit var mStorageRef: StorageReference

    private lateinit var photo: Photo

    private var isActionBarVisible: Boolean = true

    private var mainActivity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_from_gallery_preview)
        //supportActionBar?.hide()
        supportActionBar?.show()
        isActionBarVisible = true
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("")

        //Auth
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser?.uid.toString()

        //Database
        val firebase: FirebaseDatabase = FirebaseDatabase.getInstance()
        refDatabase = firebase.getReference("$userID")

        mStorageRef = FirebaseStorage.getInstance().reference

        auth.addAuthStateListener {
            if(auth.currentUser == null){
                this.finish()
            }
        }

        refDbListOfPhotos = refDatabase.child("listOfPhotos")

        IV_previewSinglePhotoGallery.setOnClickListener {
            if (isActionBarVisible) {
                supportActionBar?.hide()
                isActionBarVisible = false
            } else {
                supportActionBar?.show()
                isActionBarVisible = true
            }
        }

        photo = Photo(intent.getStringExtra("urlP"), intent.getStringExtra("idAlbumP"), intent.getStringExtra("nameP"))

        Picasso.get().load(photo.urlToFile).error(R.drawable.ic_baseline_block_24).placeholder(R.drawable.ic_baseline_cloud_download_24).into(IV_previewSinglePhotoGallery)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_menu_photo_gallery_preview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)

        when (item?.itemId) {
            android.R.id.home -> { onBackPressed() }
            R.id.app_bar_change_album -> {

            }
            R.id.app_bar_delete_photo -> {
                alertAndDeleteFromDatabaseAndStorage()
            }
        }

        return true
    }

    private fun alertAndDeleteFromDatabaseAndStorage() {

        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.delete_photo_question))
            .setMessage(resources.getString(R.string.not_back))
            .setNegativeButton(resources.getString(R.string.no)) { dialog, which ->
                // nothing
            }
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, which ->
                val mStorageRefUser: StorageReference = mStorageRef.child(userID)
                val mStorageRefUserImages = mStorageRefUser.child("images")
                val desertDeleteRef = mStorageRefUserImages.child(photo.photoName)

                val deleteTask = desertDeleteRef.delete()
                deleteTask.addOnSuccessListener(object: OnSuccessListener<Void?> {
                    override fun onSuccess(dataResult: Void?) {
                        Log.d("test","Photo successfully removed from Firebase Storage")
                        refDbListOfPhotos.child(photo.albumId).child(photo.photoName.dropLast(4)).removeValue().addOnSuccessListener(object : OnSuccessListener<Void?> {
                            override fun onSuccess(p0: Void?) {
                                Log.d("test","Post about photo deleted from Firebase Realtime Database")
                            }
                        })
                        Toast.makeText(mainActivity, resources.getString(R.string.p_photo_deleted), Toast.LENGTH_LONG).show()
                        finish()
                    }
                }).addOnFailureListener(object : OnFailureListener {
                    override fun onFailure(p0: Exception) {
                        Log.d("test","Error during removing photo from Firebase Storage")
                        Toast.makeText(mainActivity, resources.getString(R.string.e_photo_deleted_fail), Toast.LENGTH_LONG).show()
                    }
                })

            }
            .show()

    }

}