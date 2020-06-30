package com.example.myphotostock

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_image_preview.*
import java.io.File
import java.lang.Exception



class ImagePreviewActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)
        //absolute path to the photo
        val imagePath=intent.getStringExtra("imagePath")


        if ( isStoragePermissionGranted()){
            try {
                //val inputStream = Uri.fromFile(file)
              //val bitmap=BitmapFactory.decodeStream(contentResolver.openInputStream(inputStream))
                Handler().postDelayed({val bitmap=BitmapFactory.decodeFile(imagePath)

                    image_preview.setImageBitmap(bitmap)},2000)

            }catch (e:Exception){
                Log.e("ImagePreviewActivity",e.toString())
            }


        }

        button_delete.setOnClickListener {
            val image=File(imagePath)
            image.delete()
            finish()
        }
        button_accept.setOnClickListener{
            // upload image to firebase



            finish()
        }


    }
    private fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            true
        }
    }


}