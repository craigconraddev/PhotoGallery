package com.bignerdranch.android.photogallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.bignerdranch.android.photogallery.repository.FlickrDataSource

class PhotoGalleryViewModel() : ViewModel() {

    val flow =  Pager(PagingConfig(pageSize = 20)) {
        FlickrDataSource()
    }
        .flow
        .cachedIn(viewModelScope)

}