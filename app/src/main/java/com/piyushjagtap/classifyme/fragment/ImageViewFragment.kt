package com.piyushjagtap.classifyme.fragment

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.piyushjagtap.classifyme.databinding.FragmentImageViewBinding
import com.piyushjagtap.classifyme.ml.ImageClassificationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class ImageViewFragment(var imageUri: Uri,var imageLabel:String) : Fragment() {

    private var _binding: FragmentImageViewBinding? = null
    private val binding get() = _binding!!

//    private val viewModel   = ViewModelProvider(this).get(ImageInfoViewModel::class.java)

    companion object {
        private const val TAG = "ImageViewFragment"
        lateinit var bitmap: Bitmap
        private const val MIME = "text/html"
        private const val ENCODING = "UTF-8"
        private const val  INSTANCE_KEY = "ImageLabel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentImageViewBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //This Method needs to be overriden for the fragment to inflate the view.
        Log.d(TAG, "onViewCreated: ")
        Toast.makeText(context, "Fragment Image", Toast.LENGTH_SHORT).show()
        binding.imageView.setImageURI(imageUri)
        binding.imageLabel.text = imageLabel
//        Log.d(TAG, "onCreateView: $imageUri")

//        try {
//            GlobalScope.launch(Dispatchers.Default) {
//                val result = runModel(imageUri)
//                withContext(Dispatchers.Main) {
//                    Log.d(TAG, "Coroutine Result: $result")
//                    binding.imageLabel.text = result
////                    savedInstanceState!!.putString(INSTANCE_KEY,result)
////                    viewModel.setText(result)
//                }
//            }
//        } catch (e: Exception) {
//            Log.e(TAG, "Model Error: $e")
//        }
    }

//    private fun runModel(imageUri: Uri): String {
//        bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, imageUri)
//        val labels =
//            activity!!.application.assets.open("labels.txt").bufferedReader().use { it.readText() }
//                .split("\n")
//        var resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
//        val model = ImageClassificationModel.newInstance(context!!)
//
//        var tbuffer = TensorImage.fromBitmap(resized)
//        var byteBuffer = tbuffer.buffer
//
//// Creates inputs for reference.
//        val inputFeature0 =
//            TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
//        inputFeature0.loadBuffer(byteBuffer)
//
//// Runs model inference and gets result.
//        val outputs = model.process(inputFeature0)
//        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
//        var max = getMax(outputFeature0.floatArray)
//        Log.d(TAG, "Model Output : $outputFeature0")
//        return labels[max]
////        GetImageInfoAsyncTask().execute()
//
//// Releases model resources if no longer used.
//        model.close()
//    }
//
//    private fun getMax(arr: FloatArray): Int {
//        var ind = 0;
//        var min = 0.0f;
//
//        for (i in 0..1000) {
//            if (arr[i] > min) {
//                min = arr[i]
//                ind = i;
//            }
//        }
//        return ind
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: ")
        _binding = null
    }
}