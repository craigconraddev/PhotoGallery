package com.bignerdranch.android.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class PhotoGalleryViewModel() : ViewModel() {

    var galleryItemLiveData: LiveData<List<GalleryItem>>

    private val flickrFetcher = FlickrFetcher()
    private val mutableSearchTerm = MutableLiveData<String>()

    init {
        mutableSearchTerm.value = "planets"
        galleryItemLiveData = Transformations.switchMap(mutableSearchTerm) { searchTerm ->
            flickrFetcher.searchPhotos(searchTerm)
        }
    }

    fun fetchPhotos(query: String = "") {
        mutableSearchTerm.value = query
    }
}