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
        private var elementList: ElementListItem? = null
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

        fun bindSet(elementList: ElementListItem) {
            this.elementList = elementList
            binding.webTitleTxt.text = elementList.linkTitle
            binding.webLinkTxt.text = elementList.linkUrl
            binding.webDescriptionTxt.text = elementList.linkDescription

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageInfoViewHolder {
//        val itemView =
//            LayoutInflater.from(parent.context).inflate(R.layout.item_web_scrape, parent, false)
//        return ImageInfoViewHolder(itemView)
        val binding =
            ItemWebScrapeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageInfoViewHolder(binding)
        Log.d(TAG, "onCreateViewHolder: ")
    }

    override fun onBindViewHolder(holder: ImageInfoViewHolder, position: Int) {
//        val itemCard = elementLists[position]
//        holder.bindSet(itemCard)
        Log.d(TAG, "onBindViewHolder: ")
        holder.webTitle.text = elementLists[position].linkTitle
        holder.webLink.text = elementLists[position].linkUrl
        holder.webDesc.text = elementLists[position].linkDescription
//        with(holder) {
//            with(elementLists[position]) {
//                binding.webTitleTxt.text = this.linkTitle
//                binding.webLinkTxt.text = this.linkUrl
//                binding.webDescriptionTxt.text = this.linkDescription
//            }
//        }
    }

    override fun getItemCount(): Int {
        return elementLists.size
    }
}