package com.piyushjagtap.classifyme.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.piyushjagtap.classifyme.databinding.ImageInfoFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class ImageInfoFragment(var imageLabel: String) : Fragment() {

    private var _binding: ImageInfoFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val TAG = "ImageInfoFragment"
        private const val MIME = "text/html"
        private const val ENCODING = "UTF-8"
    }

    private lateinit var viewModel: ImageInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = ImageInfoFragmentBinding.inflate(inflater, container, false)
        return binding.root
//        inflater.inflate(R.layout.image_info_fragment, container, false)
        Log.d(TAG, "onCreateView: ")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        binding.text.text = imageLabel
        var doc:Document? = null
        try {
            GlobalScope.launch(Dispatchers.Default) {
                doc =  Jsoup.connect("https://www.google.com/search?q=image+classification").get()
//                val result = runModel(imageURI)
                withContext(Dispatchers.Main) {
                    val element:Elements = doc!!.getElementsByClass("tF2Cxc")
                    binding.webView.loadData(element.toString(), MIME, ENCODING)
                    Log.d(TAG, "WebView: $element")
//                    Log.d(TAG, "Coroutine Result: $result")
//                    imageLabel = result
//                    binding.imageLabel.text = result
//                    savedInstanceState!!.putString(INSTANCE_KEY,result)
//                    viewModel.setText(result)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Model Error: $e")
        }
//        var label = savedInstanceState!!.getString("ImageLabel")
//        Log.d(TAG, "onActivityCreated: $label")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated: ")
//        viewModel = ViewModelProvider(this).get(ImageInfoViewModel::class.java)
//        viewModel.getText()!!.observe(viewLifecycleOwner, Observer {
//            Log.d(TAG, "onActivityCreated: $it")
//        })
    }

}