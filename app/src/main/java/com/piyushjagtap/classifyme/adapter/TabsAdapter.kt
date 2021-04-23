package com.piyushjagtap.classifyme.adapter

import android.content.Context
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.piyushjagtap.classifyme.fragment.ImageInfoFragment
import com.piyushjagtap.classifyme.fragment.ImageViewFragment

//Adapter to set Fragments inside ImageViewActivity as Tab Layout
class TabsAdapter(private val context: Context, fm: FragmentManager, behavior: Int) :
    FragmentPagerAdapter(fm, behavior) {
    lateinit var imageUrl: Uri
    lateinit var imageLabel: String

    //Getting ImageURI and Image Label Data from activity
    open fun setData(imageUrl: Uri, imageLabel: String) {
        this.imageUrl = imageUrl
        this.imageLabel = imageLabel
    }

    override fun getCount(): Int {
        return 2
    }

    //Setting Fragments on Tab Layout
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                //Calling ImageViewFragment and parsing imageUri and imageLabel
                ImageViewFragment(imageUrl, imageLabel)
            }
            else -> {
                //Calling ImageInfoFragment and parsing  imageLabel
                ImageInfoFragment(imageLabel)
            }
        }
    }
}