package com.piyushjagtap.classifyme.adapter

import android.content.Context
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.piyushjagtap.classifyme.fragment.ImageInfoFragment
import com.piyushjagtap.classifyme.fragment.ImageViewFragment

class TabsAdapter(private val context: Context, fm: FragmentManager, behavior: Int) :
    FragmentPagerAdapter(fm, behavior) {
    lateinit var imageUrl: Uri
    lateinit var imageLabel: String

    open fun setData(imageUrl: Uri, imageLabel: String) {
        this.imageUrl = imageUrl
        this.imageLabel = imageLabel
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ImageViewFragment(imageUrl,imageLabel)
            }
            else -> {
                ImageInfoFragment(imageLabel)
            }
        }
    }
}