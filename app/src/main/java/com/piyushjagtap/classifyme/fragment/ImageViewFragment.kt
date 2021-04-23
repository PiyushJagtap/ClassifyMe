package com.piyushjagtap.classifyme.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.piyushjagtap.classifyme.databinding.FragmentImageViewBinding
import com.squareup.picasso.Picasso


class ImageViewFragment(var imageUri: Uri, var imageLabel: String) : Fragment() {

    //ImageViewFragment View Binding in fragment
    private var _binding: FragmentImageViewBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val TAG = "ImageViewFragment"
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
        Log.d(TAG, "onViewCreated: $imageUri, $imageLabel")
        //Set image on image view  from image uri
        Picasso.get()
            .load(imageUri)
            .fit()
            .centerInside()
            .into(binding.imageView)

        //Set image label
        binding.imageLabel.text = imageLabel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: ")
        _binding = null
    }
}