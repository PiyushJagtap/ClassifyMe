package com.piyushjagtap.classifyme

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.piyushjagtap.classifyme.databinding.ActivityImageViewBinding

class ImageViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageViewBinding

    companion object {
        private const val TAG = "ImageViewActivity"
        private var imageString: String? = null
        private var imageURI: Uri? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        val viewBinding = binding.root
        setContentView(viewBinding)
        imageString = intent.getStringExtra("imageURI")
        Log.d(TAG, "Image String : $imageString")
        imageURI = Uri.parse(imageString)
        Log.d(TAG, "Image URI : $imageURI")
//        binding.imageView.setImageURI(imageURI)
        var bitmap:Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageURI)
        binding.imageView.setImageBitmap(bitmap)
//        binding.imageView.setImageURI(imageURI)
    }
}