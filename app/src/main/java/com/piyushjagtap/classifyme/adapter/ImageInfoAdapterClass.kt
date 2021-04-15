package com.piyushjagtap.classifyme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.piyushjagtap.classifyme.R

class ImageInfoAdapterClass : RecyclerView.Adapter<ImageInfoAdapterClass.ImageInfoViewHolder>() {

    class ImageInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        override fun onClick(v: View?) {
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageInfoViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.bottom_sheet_item, parent, false)
        return  ImageInfoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageInfoViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}