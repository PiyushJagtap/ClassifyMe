package com.piyushjagtap.classifyme

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
        binding.imageView.setImageURI(imageURI)
//        if (savedInstanceState == null) {
//            Log.d(TAG, "FRAGMENT: ")
//            supportFragmentManager
//                .beginTransaction()
//                .replace(
//                    R.id.imageInfoContainer, ItemLinksListDialogFragment()
//                )
//                .addToBackStack(null)
//                .commit()
//        }
    }
}