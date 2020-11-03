package com.bignerdranch.android.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

class PhotoGalleryViewModel() : ViewModel() {

    val galleryItemLiveData = liveData(Dispatchers.IO) {
        val retrieved = FlickrFetcher().fetchPhotos()
        emit(retrieved)
    }
}