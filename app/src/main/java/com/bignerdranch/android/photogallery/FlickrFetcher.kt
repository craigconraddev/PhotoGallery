package com.bignerdranch.android.photogallery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.photogallery.api.FlickrApi
import com.bignerdranch.android.photogallery.api.FlickrResponse
import com.bignerdranch.android.photogallery.api.PhotoResponse
import com.google.gson.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

private const val TAG = "FlickrFetcher"

class FlickrFetcher {

    private val flickrApi: FlickrApi

    init {
        val gson : Gson = GsonBuilder()
            .registerTypeAdapter(PhotoResponse::class.java, PhotoDeserializer())
            .create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    fun fetchPhotos(): LiveData<List<GalleryItem>> {
        val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()
        val flickrRequest: Call<FlickrResponse> = flickrApi.fetchPhotos()

        flickrRequest.enqueue(object: Callback<FlickrResponse> {

            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }

            override fun onResponse(call: Call<FlickrResponse>, response: Response<FlickrResponse>) {
                Log.d(TAG, "Response received")
                val flickrResponse: FlickrResponse? = response.body()
                val photoResponse: PhotoResponse? = flickrResponse?.photos
                var galleryItems: List<GalleryItem> = photoResponse?.galleryItems
                    ?: mutableListOf()
                galleryItems = galleryItems.filterNot {
                    it.url.isBlank()
                }
                responseLiveData.value = galleryItems
            }
        })
        return responseLiveData
    }

    class PhotoDeserializer: JsonDeserializer<PhotoResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): PhotoResponse {
            val photoObject = json?.asJsonObject!!
            val galleryItems = mutableListOf<GalleryItem>()
            val photoArray = photoObject.get("photo").asJsonArray
            photoArray.forEach { photo ->
                val galleryItem = GalleryItem()
                val photoElement = photo.asJsonObject
                galleryItem.title = photoElement.get("title").asString;
                galleryItem.id = photoElement.get("id").asString;
                galleryItem.url = photoElement.get("url_s").asString;
                galleryItems.add(galleryItem)
            }
           val photoResponse = PhotoResponse()
            photoResponse.galleryItems = galleryItems
            return photoResponse
        }
    }
}