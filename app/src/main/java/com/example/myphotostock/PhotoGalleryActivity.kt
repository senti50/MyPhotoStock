package com.example.myphotostock

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils


class PhotoGalleryActivity : AppCompatActivity() {

    private lateinit var idAlbum: String
    private lateinit var titleAlbum: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idAlbum = intent.getStringExtra("idAlbum")
        titleAlbum = intent.getStringExtra("titleAlbum")

        setContentView(R.layout.activity_photo_gallery)

        setTitle(titleAlbum)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

}