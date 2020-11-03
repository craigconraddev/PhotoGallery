package com.bignerdranch.android.photogallery.api

import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
    @GET("services/rest/?method=flickr.interestingness.getlist" +
            "&api_key=2d337a5a2b7171e06a223fc659692022" +
            "&format=json" +
            "&nojsoncallback=1" +
            "&extras=url_s")
    suspend fun fetchPhotos(@Query("page") page: Int): FlickrResponse
}