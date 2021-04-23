package com.piyushjagtap.classifyme

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.piyushjagtap.classifyme.databinding.ActivityMainBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    //MainActivity View Binding
    private lateinit var binding: ActivityMainBinding

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    companion object {
        private const val TAG = "MainActivity"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE
        )
        private const val PICK_IMAGE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.semi_transparent)))
        val view = binding.root
        setContentView(view)

        // Request  permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        //Check internet connection
        Thread {                        //background thread
            val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
            Log.d(TAG, "Internet Connection: $isConnected")
            if (!isConnected) {
                Toast.makeText(this, "Please Turn on the Internet Connection", Toast.LENGTH_SHORT)
                    .show()
            }
        }.run()

        //Camera Capture Button Listener
        binding.cameraCaptureButton.setOnClickListener {
            Toast.makeText(this, "Image Clicked!!!", Toast.LENGTH_SHORT).show()
            takePhoto(savedInstanceState)
        }

        //Image From File manager Button Listener
        binding.galleryImageButton.setOnClickListener {
            Toast.makeText(this, "Opening FIle Manager", Toast.LENGTH_SHORT).show()
            openGallery()
        }

        //Create a directory for storing photos if doesn't exists.
        outputDirectory = getOutputDirectory()
        if (outputDirectory.exists()) {
            Log.d(TAG, "Directory Success : " + outputDirectory.absolutePath)
        } else {
            Log.d(TAG, "Directory: $outputDirectory")
            outputDirectory = getOutputDirectory()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    //Click Photo and save it in directory function
    private fun takePhoto(savedInstanceState: Bundle?) {
        if (!binding.progressCircular.isVisible) {
            binding.progressCircular.visibility = View.VISIBLE
        }
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + getString(R.string.app_name) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    openImageView(savedUri)
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            })
    }

    //Start hardware camera and view on ViewPager
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.createSurfaceProvider())
                }

            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    //Get directory path
    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    //Permission Requested Result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    //Open the saved image from camera or from the File intent in ImageViewActivity and send ImageURI
    private fun openImageView(imgURI: Uri) {
        binding.progressCircular.visibility = View.VISIBLE
        Thread.sleep(2000)
        Log.d(TAG, "openImageView: ${imgURI.toString()}")
        val intent = Intent(this, ImageViewActivity::class.java).apply {
            action = Intent.ACTION_SEND
            putExtra("imageURI", imgURI.toString())
        }
        if (binding.progressCircular.isVisible) {
            binding.progressCircular.visibility = View.INVISIBLE
        }
        binding.progressCircular.visibility = View.INVISIBLE
        startActivity(intent)
    }

    //Open File Manager or gallery via Intent
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }

    //Get Result of the file manager intent
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            val imageUri = data?.data
            Log.d(TAG, "onActivityResult: $imageUri")
            openImageView(imageUri!!)
        } else {
            Log.d(TAG, "onActivityResult: $resultCode")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}