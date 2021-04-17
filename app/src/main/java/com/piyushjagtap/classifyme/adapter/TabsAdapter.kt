package com.piyushjagtap.classifyme.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.piyushjagtap.classifyme.fragment.ImageInfoFragment
import com.piyushjagtap.classifyme.fragment.ImageViewFragment

class TabsAdapter(private val context: Context, fm: FragmentManager, private var behavior: Int) :
    FragmentPagerAdapter(fm, behavior) {
    override fun getCount(): Int {
        return behavior
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ImageViewFragment()
            }
            1 -> {
                ImageInfoFragment()
            }
            else -> null!!
        }
    }
}