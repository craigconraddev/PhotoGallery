package com.bignerdranch.android.photogallery.repository

import androidx.paging.PagingSource
import com.bignerdranch.android.photogallery.FlickrFetcher
import com.bignerdranch.android.photogallery.GalleryItem

class FlickrDataSource() : PagingSource<Int, GalleryItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryItem> {
        return try {
            val nextPage = params.key ?: 1
            val response = FlickrFetcher().fetchPhotos(nextPage)
            LoadResult.Page(
                data = response,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

}