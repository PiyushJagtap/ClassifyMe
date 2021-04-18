package com.piyushjagtap.classifyme.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.piyushjagtap.classifyme.R

class ImageInfoFragment : Fragment() {

    companion object {
        private const val TAG = "ImageInfoFragment"
        fun newInstance() = ImageInfoFragment()
    }

    private lateinit var viewModel: ImageInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.image_info_fragment, container, false)
        Log.d(TAG, "onCreateView: ")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
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