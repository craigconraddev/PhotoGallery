package com.bignerdranch.android.photogallery

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.floor

private const val TAG = "PhotoGalleryFragment"

class PhotoGalleryFragment
    : Fragment() {
    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var photoGalleryViewModel: PhotoGalleryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photoGalleryViewModel = ViewModelProvider(this).get(PhotoGalleryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)

        photoRecyclerView = view.findViewById(R.id.photo_recycler_view)
       // photoRecyclerView.layoutManager = GridLayoutManager(context, 3)

        photoRecyclerView.doOnLayout {
            val spanCount = calculateSize()
            Log.d(TAG, "spanCount: $spanCount")
            photoRecyclerView.layoutManager = GridLayoutManager(context, spanCount)
            //(photoRecyclerView.layoutManager as GridLayoutManager).spanCount = spanCount
            photoRecyclerView.requestLayout()
        }

        return view
    }

    fun calculateSize(): Int {
        return floor((photoRecyclerView.width / convertDPToPixels(120)).toDouble()).toInt()
    }

    private fun convertDPToPixels(dp : Int): Float {
        val metrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(metrics)
        return (dp *  metrics.density).toFloat()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoGalleryViewModel.galleryItemLiveData.observe(
            viewLifecycleOwner,
            Observer { galleryItems ->
                Log.d(TAG, "Response received: $galleryItems")
                photoRecyclerView.adapter = PhotoAdapter(galleryItems)
        })
    }

    private class PhotoHolder(itemTextView: TextView)
        :RecyclerView.ViewHolder(itemTextView) {
        val bindTitle: (CharSequence) -> Unit = itemTextView::setText
    }

    private class PhotoAdapter(private val galleryItems: List<GalleryItem>)
        :RecyclerView.Adapter<PhotoHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            val textView = TextView(parent.context)
            return PhotoHolder(textView)
        }

        override fun getItemCount(): Int = galleryItems.size

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = galleryItems[position]
            holder.bindTitle(galleryItem.title)
        }
    }

    companion object {
        fun newInstance() = PhotoGalleryFragment()
    }
}