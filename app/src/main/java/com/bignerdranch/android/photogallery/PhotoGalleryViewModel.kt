package com.bignerdranch.android.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class PhotoGalleryViewModel() : ViewModel() {

    private var flickrFetcher = FlickrFetcher()
    var galleryItemLiveData: LiveData<List<GalleryItem>>

    init {
        galleryItemLiveData = flickrFetcher.fetchPhotos()
    }

    override fun onCleared() {
        super.onCleared()
        flickrFetcher.cancelRequestInFlight()
    }
}