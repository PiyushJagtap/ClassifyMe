package com.piyushjagtap.classifyme

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.piyushjagtap.classifyme.adapter.TabsAdapter
import com.piyushjagtap.classifyme.databinding.ActivityImageViewBinding
import com.piyushjagtap.classifyme.ml.ImageClassificationModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer


class ImageViewActivity : AppCompatActivity() {

    //ImageViewActivity View Binding
    private lateinit var binding: ActivityImageViewBinding

    companion object {
        private const val TAG = "ImageViewActivity"
        private var imageString: String? = null
        private lateinit var imageURI: Uri
        private lateinit var bitmap: Bitmap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        val viewBinding = binding.root
        setContentView(viewBinding)

        //Getting image from intent
        imageString = intent.getStringExtra("imageURI")
        Log.d(TAG, "Image String : $imageString")
        imageURI = Uri.parse(imageString)
        Log.d(TAG, "Image URI : $imageURI")

        //Calling the model function
        val result = runModel(imageURI)
        Log.d(TAG, "Model Result: $result")

        //Setting Tab Layout with ViewPager
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Image")
            .setIcon(R.drawable.ic_twotone_image_search_24))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Related Info...")
            .setIcon(R.drawable.ic_twotone_list_alt_24))
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val tabAdapter = TabsAdapter(this, supportFragmentManager, binding.tabLayout.tabCount)
        tabAdapter.setData(imageURI, result) //Send image URI and Image Label to adapter
        binding.viewPager.adapter = tabAdapter

        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    //Function To Run Model
    private fun runModel(imageUri: Uri): String {
        var result: String? = null
        try {
            bitmap =
                MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            val labels =
                application.assets.open("labels.txt").bufferedReader().use { it.readText() }
                    .split("\n")
            var resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            val model = ImageClassificationModel.newInstance(this)

            var tbuffer = TensorImage.fromBitmap(resized)
            var byteBuffer = tbuffer.buffer

// Creates inputs for reference.
            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
            inputFeature0.loadBuffer(byteBuffer)

// Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            var max = getMax(outputFeature0.floatArray)
            Log.d(TAG, "Model Output : $outputFeature0")
            result = labels[max]

// Releases model resources if no longer used.
            model.close()
        } catch (e: Exception) {
            Log.e(TAG, "runModel: $e")
        }
        return result!!
    }

    private fun getMax(arr: FloatArray): Int {
        var ind = 0;
        var min = 0.0f;

        for (i in 0..1000) {
            if (arr[i] > min) {
                min = arr[i]
                ind = i;
            }
        }
        return ind
    }
}