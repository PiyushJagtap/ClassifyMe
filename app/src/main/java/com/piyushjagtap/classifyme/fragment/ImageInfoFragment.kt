package com.piyushjagtap.classifyme.fragment

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.piyushjagtap.classifyme.adapter.RecyclerViewAdapter
import com.piyushjagtap.classifyme.databinding.ImageInfoFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import kotlin.concurrent.thread

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
        Log.d(TAG, "onCreateView: ")
        try {
            GetImageElementsAsyncTask(imageLabel).execute()
        } catch (e: Exception) {
            Log.e(TAG, "Web Scraping Error: $e")
        }
        return binding.root
//        inflater.inflate(R.layout.image_info_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
//        binding.text.text = imageLabel
//        GetImageElementsAsyncTask().execute()
//        thread {
//            val doc =
//                Jsoup.connect("https://www.google.com/search?q=image+classification").get()
//            val linkLists = doc!!.getElementsByClass("eqAnXb")
//            val divElements = linkLists[0].getElementsByClass("tF2Cxc")
//            Log.d(TAG, "Main: $linkLists")
//            Log.d(TAG, "Div: $divElements")
//            val elementsList = ArrayList<ElementListItem>()
//
//            for (divElement in divElements) {
//                val linkTitle = divElement.getElementsByTag("h3")[0].ownText().toString()
//                val linkUrl = divElement.getElementsByTag("a")[0].absUrl("href")
//                val linkDescription = divElement.getElementsByTag("span").text().toString()
//                Log.d(TAG,
//                    "Div Elements:Title: $linkTitle \n Url: $linkUrl \n Desc: $linkDescription")
//                elementsList.add(ElementListItem(linkTitle, linkUrl, linkDescription))
//            }
//            activity!!.runOnUiThread {
//                val recyclerViewAdapter = RecyclerViewAdapter(elementsList)
//                val linearLayoutManager = LinearLayoutManager(context)
//                binding.recyclerView.layoutManager = linearLayoutManager
//                binding.recyclerView.adapter = recyclerViewAdapter
//            }
//        }
//            GlobalScope.launch(Dispatchers.Default) {
//                val doc =
//                    Jsoup.connect("https://www.google.com/search?q=image+classification").get()
//                val linkLists = doc!!.getElementsByClass("eqAnXb")
//                val divElements = linkLists[0].getElementsByClass("tF2Cxc")
//                Log.d(TAG, "Main: $linkLists")
//                Log.d(TAG, "Div: $divElements")
//                val elementsList = ArrayList<ElementListItem>()
//
//                for (divElement in divElements) {
//                    val linkTitle = divElement.getElementsByTag("h3")[0].ownText().toString()
//                    val linkUrl = divElement.getElementsByTag("a")[0].absUrl("href")
//                    val linkDescription = divElement.getElementsByTag("span").text().toString()
//                    Log.d(TAG,
//                        "Div Elements:Title: $linkTitle \n Url: $linkUrl \n Desc: $linkDescription")
//                    elementsList.add(ElementListItem(linkTitle, linkUrl, linkDescription))
//                }
//                withContext(Dispatchers.Main) {
//                    val recyclerViewAdapter = RecyclerViewAdapter(elementsList)
//                    val linearLayoutManager = LinearLayoutManager(context)
//                    binding.recyclerView.layoutManager = linearLayoutManager
//                    binding.recyclerView.adapter = recyclerViewAdapter
//                }
//            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated: ")
    }

    inner class GetImageElementsAsyncTask(var imageLabel: String) :
        AsyncTask<Void, Int, ArrayList<ElementListItem>>() {
        override fun onPreExecute() {
            super.onPreExecute()
            binding.progressCircular.visibility = View.VISIBLE
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            Log.d(TAG, "onProgressUpdate: $values")
            binding.progressCircular.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: Void?): ArrayList<ElementListItem> {
            val doc =
                Jsoup.connect("https://www.google.com/search?q=$imageLabel").get()
            val linkLists = doc!!.getElementsByClass("eqAnXb")
            val divElements = linkLists[0].getElementsByClass("tF2Cxc")
            Log.d(TAG, "Main: $linkLists")
            Log.d(TAG, "Div: $divElements")
            val elementsList = ArrayList<ElementListItem>()

            for (divElement in divElements) {
                if (divElement != null && divElement.childrenSize() != 0) {
                    val linkTitle = divElement.getElementsByTag("h3")[0].ownText().toString()
                    val linkUrl = divElement.getElementsByTag("a")[0].absUrl("href")
//                val linkDescription = divElement.getElementsByTag("span")[0].text().toString()
                    val linkDescription =
                        divElement.getElementsByClass("aCOpRe")[0].text().toString()
                    when {
                        linkTitle.isNullOrEmpty() -> {
                            Log.d(TAG,
                                "Div Elements:Title: $linkTitle \n Url: $linkUrl \n Desc: $linkDescription")
                            elementsList.add(ElementListItem("Title Not Available",
                                linkUrl,
                                linkDescription))
                        }
                        linkUrl.isNullOrEmpty() -> {
                            Log.d(TAG,
                                "Div Elements:Title: $linkTitle \n Url: $linkUrl \n Desc: $linkDescription")
                            elementsList.add(ElementListItem(linkTitle,
                                "URL Not Available",
                                linkDescription))
                        }
                        linkDescription.isNullOrEmpty() -> {
                            Log.d(TAG,
                                "Div Elements:Title: $linkTitle \n Url: $linkUrl \n Desc: $linkDescription")
                            elementsList.add(ElementListItem(linkTitle,
                                linkUrl,
                                "Description Not Available"))
                        }
                        else -> {
                            Log.d(TAG,
                                "Div Elements:Title: $linkTitle \n Url: $linkUrl \n Desc: $linkDescription")
                            elementsList.add(ElementListItem(linkTitle, linkUrl, linkDescription))
                        }
                    }
                }
            }
            return elementsList
        }

        override fun onPostExecute(result: ArrayList<ElementListItem>) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()) {
                binding.progressCircular.visibility = View.INVISIBLE
                Log.d(TAG, "onPostExecute: ${result.size}")
                val recyclerViewAdapter = RecyclerViewAdapter(result)
                recyclerViewAdapter.notifyDataSetChanged()
                val linearLayoutManager = LinearLayoutManager(context)
                binding.recyclerView.layoutManager = linearLayoutManager
                binding.recyclerView.setHasFixedSize(true)
                binding.recyclerView.adapter = recyclerViewAdapter
            }
        }
    }

}