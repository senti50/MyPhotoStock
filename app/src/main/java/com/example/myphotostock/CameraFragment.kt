package com.example.myphotostock

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myphotostock.databinding.FragmentCameraBinding
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors


private const val REQUEST_CODE_PERMISSIONS = 1

private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
class CameraFragment : Fragment() {
    companion object{
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"

        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
            }
            return if (mediaDir != null && mediaDir.exists())mediaDir else appContext.filesDir
        }

        fun createFile(baseFolder: File, format: String, extension: String) =
            File(baseFolder, SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + extension)
    }
    private var _binding: FragmentCameraBinding?=null
    private lateinit var imageCapture: ImageCapture
    private val binding get() = _binding!!
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var previewView: PreviewView
    private lateinit var imagePreview: Preview
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var outputDirectory: File
    var imagePath=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_camera, container, false)
        _binding=FragmentCameraBinding.inflate(inflater,container,false)
        val view=binding.root
        previewView = binding.previewView

        cameraProviderFuture = ProcessCameraProvider.getInstance(context!!)
        if (allPermissionsGranted()) {
            previewView.post { startCamera() }
        } else {
            requestPermissions(
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        outputDirectory = getOutputDirectory(requireContext())
        binding.cameraCaptureButton.setOnClickListener {
            takePicture()
            previewView.post {
                Toast.makeText(context, imagePath, Toast.LENGTH_LONG).show()
            }
            Log.i("FRAGMENT",imagePath)
            activity?.let{
                val intent = Intent (activity, ImagePreviewActivity::class.java)
                intent.putExtra("imagePath",imagePath)
                startActivity(intent)

            }

        }


        return view
    }


    private fun takePicture() {
        val file = createFile(
            outputDirectory,
            FILENAME,
            PHOTO_EXTENSION
        )

        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture.takePicture(outputFileOptions, executor, object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {


            }

            override fun onError(exception: ImageCaptureException) {
                val msg = "Photo capture failed: ${exception.message}"
                Log.e("CameraFragment", "Photo capture failed: ${exception.message}", exception)
                previewView.post {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                }
            }
        })
        imagePath=file.absolutePath

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
    private fun startCamera() {

        imagePreview = Preview.Builder().apply {
            setTargetAspectRatio(AspectRatio.RATIO_16_9)
            setTargetRotation(previewView.display.rotation)
        }.build()
        imageCapture = ImageCapture.Builder().apply {
           setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        }.build()
        val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.bindToLifecycle(this, cameraSelector, imagePreview, imageCapture)
            previewView.preferredImplementationMode = PreviewView.ImplementationMode.TEXTURE_VIEW
            imagePreview.setSurfaceProvider(previewView.createSurfaceProvider())
        }, ContextCompat.getMainExecutor(context))
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(context!!, it) == PackageManager.PERMISSION_GRANTED
    }
}