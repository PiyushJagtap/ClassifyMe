package com.piyushjagtap.classifyme.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.piyushjagtap.classifyme.R
import com.piyushjagtap.classifyme.databinding.ItemWebScrapeBinding
import com.piyushjagtap.classifyme.fragment.ElementListItem

class RecyclerViewAdapter(private val elementLists: List<ElementListItem>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ImageInfoViewHolder>() {

    companion object {
        private const val TAG = "RecyclerViewAdapter"
    }

    inner class ImageInfoViewHolder(val binding: ItemWebScrapeBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        private var view: View = binding.root
        var webTitle: TextView
        var webLink: TextView
        var webDesc: TextView

        init {
            itemView.setOnClickListener(this)
            webTitle = view.findViewById(R.id.web_title_txt)
            webLink = view.findViewById(R.id.web_link_txt)
            webDesc = view.findViewById(R.id.web_description_txt)
        }

        override fun onClick(v: View?) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageInfoViewHolder {
        val binding =
            ItemWebScrapeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.d(TAG, "onCreateViewHolder: ")
        return ImageInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageInfoViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ")
        holder.webTitle.text = elementLists[position].linkTitle
        holder.webLink.text = elementLists[position].linkUrl
        holder.webDesc.text = elementLists[position].linkDescription
    }

    override fun getItemCount(): Int {
        return elementLists.size
    }
}