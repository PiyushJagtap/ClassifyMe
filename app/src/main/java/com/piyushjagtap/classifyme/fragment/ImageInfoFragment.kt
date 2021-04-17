package com.piyushjagtap.classifyme.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.piyushjagtap.classifyme.R

class ImageInfoFragment : Fragment() {

    companion object {
        fun newInstance() = ImageInfoFragment()
    }

    private lateinit var viewModel: ImageInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.image_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ImageInfoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}