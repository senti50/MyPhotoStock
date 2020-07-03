package com.example.myphotostock

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.SensorManager
import android.media.VolumeShaper
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Surface
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_image_preview.*
import java.io.File


class ImagePreviewActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)
        supportActionBar?.hide()
        //absolute path to the photo
        val imagePath=intent.getStringExtra("imagePath")


        if ( isStoragePermissionGranted()){
            try {
                //val inputStream = Uri.fromFile(file)
              //val bitmap=BitmapFactory.decodeStream(contentResolver.openInputStream(inputStream))
                Handler().postDelayed({val bitmap=BitmapFactory.decodeFile(imagePath)
                    val rotatedImage = rotatebitmap(bitmap)
                    image_preview.setImageBitmap(rotatedImage)},2000)

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
            //TODO: upload image to firebase

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

    private fun rotatebitmap(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        val rot: Float
        val screenOrientation = (this.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.orientation
        when(screenOrientation) {
            Surface.ROTATION_0 -> rot = 90.0f
            Surface.ROTATION_90 -> rot = 360.0f
            Surface.ROTATION_180 -> rot = 0.0f
            else -> rot = 180.0f
        }
        
        matrix.postRotate(rot)

        val rotatedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true)
        return rotatedBitmap
    }


}