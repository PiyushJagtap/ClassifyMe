package com.piyushjagtap.classifyme

import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.piyushjagtap.classifyme.adapter.TabsAdapter
import com.piyushjagtap.classifyme.databinding.ActivityImageViewBinding
import com.piyushjagtap.classifyme.ml.ImageClassificationModel
import org.jsoup.nodes.Document
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer


class ImageViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageViewBinding
    private lateinit var doc: Document
    private lateinit var viewModel: ImageViewModel

    companion object {
        private const val TAG = "ImageViewActivity"
        private var imageString: String? = null
        private lateinit var imageURI: Uri
        private lateinit var bitmap: Bitmap
        private const val MIME = "text/html"
        private const val ENCODING = "UTF-8"

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
        val result = runModel(imageURI)
//        val resultAsync = GetImageLabelAsync(imageURI).execute().get()
//        Log.d(TAG, "onCreate: $resultAsync")
//        val result = GetImageLabelAsync(imageURI).execute().get()
        Log.d(TAG, "Model Result: $result")

//        try {
//            GlobalScope.launch(Dispatchers.Default) {
//                val result = runModel(imageURI)
//                withContext(Dispatchers.Main) {
//                    Log.d(TAG, "Coroutine Result: $result")
//                    imageLabel = result
////                    binding.imageLabel.text = result
////                    savedInstanceState!!.putString(INSTANCE_KEY,result)
////                    viewModel.setText(result)
//                }
//            }
//        } catch (e: Exception) {
//            Log.e(TAG, "Model Error: $e")
//        }
//        Log.d(TAG, "onCreate: Label $imageLabel")
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Image")
            .setIcon(R.drawable.ic_twotone_image_search_24))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Related Info...")
            .setIcon(R.drawable.ic_twotone_list_alt_24))
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val tabAdapter = TabsAdapter(this, supportFragmentManager, binding.tabLayout.tabCount)
        binding.viewPager.adapter = tabAdapter
        tabAdapter.setData(imageURI, result)
//        binding.tabLayout.setupWithViewPager(binding.viewPager)
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

//        binding.imageView.setImageURI(imageURI)
//        try {
//            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageURI)
//            val labels =
//                application.assets.open("labels.txt").bufferedReader().use { it.readText() }
//                    .split("\n")
//            var resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
//            val model = ImageClassificationModel.newInstance(this)
//
//            var tbuffer = TensorImage.fromBitmap(resized)
//            var byteBuffer = tbuffer.buffer
//
//// Creates inputs for reference.
//            val inputFeature0 =
//                TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
//            inputFeature0.loadBuffer(byteBuffer)
//
//// Runs model inference and gets result.
//            val outputs = model.process(inputFeature0)
//            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
//            var max = getMax(outputFeature0.floatArray)
//            Log.d(TAG, "Model Output : $outputFeature0")
//            val outputLabel = labels[max]
//            binding.imageName.text = outputLabel
//            GetImageInfoAsyncTask(outputLabel).execute()
//
//// Releases model resources if no longer used.
//            model.close()
//        } catch (e: Exception) {
//            Log.e(TAG, "Model: $e")
//        }


        //        binding.webView.loadUrl(
//            "https://www.google.com/search?q=image+classification&oq=image+classification&aqs=chrome..69i57j69i59j0l2j69i60l3j69i65.4119j0j7&sourceid=chrome&ie=UTF-8"
//        )
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
//        GetImageInfoAsyncTask().execute()

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

    //    inner class GetImageInfoAsyncTask(private val outputLabel: String) :
//        AsyncTask<Void, Void, String>() {
//        override fun onPreExecute() {
//            super.onPreExecute()
//            Toast.makeText(this@ImageViewActivity, "Loading Resources...", Toast.LENGTH_SHORT)
//                .show()
//        }
//
//        override fun doInBackground(vararg params: Void?): String {
//            val outputLabel = outputLabel
////            val inputSearchString = File("search?q=$outputLabel")
////            val document = Jsoup.parse(
////                inputSearchString, "UTF-8",
////                "https://www.google.com/"
////            )
//            val document = Jsoup.connect("https://www.google.com/search?q=$outputLabel").get()
//            Log.d(TAG, "doInBackground:document $document")
//            return document.toString()
//        }
//
//        override fun onPostExecute(result: String?) {
//            super.onPostExecute(result)
//            Log.d(TAG, "onPostExecute: result$result")
//            val document = Jsoup.parse(result)
//            Log.d(TAG, "onPostExecute: document$document")
//            val divSelection: Elements? = document.select("div.GyAeWb")
//            Log.d(TAG, "onPostExecute: element$divSelection")
//            binding.webView.loadData(divSelection.toString(), MIME, ENCODING)
//        }
//    }

    inner class GetImageLabelAsync(var imageUri: Uri): AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            return runModel(imageUri)
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: String?){
            super.onPostExecute(result)
        }

        override fun onProgressUpdate(vararg values: Void?) {
            super.onProgressUpdate(*values)
        }
    }
}