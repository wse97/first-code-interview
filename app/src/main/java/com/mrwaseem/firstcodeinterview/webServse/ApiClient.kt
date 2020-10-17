package com.ugb.mvvmkotlin.ApiClient

import android.app.Application
import android.content.Context
import com.mrwaseem.firstcodeinterview.webServse.response.RootResponse
import com.mrwaseem.firstcodeinterview.webServse.response.AddPostResponse
import com.mrwaseem.firstcodeinterview.webServse.response.PostsResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.HashMap

class ApiClient() : Application() {

    var dataClient: DataClient

    companion object {
        lateinit var mContext: Context
        val baseUrl = "https://eylul.blaady.com/api/"

        var instance: ApiClient? = null
        fun getINSTANCE(): ApiClient {
            if (instance==null) {
                instance = ApiClient()
            }
            return instance as ApiClient
        }

    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
    }

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        dataClient = retrofit.create(DataClient::class.java)
    }

    fun addNewPost(title : String , image : File): Call<AddPostResponse?>? {
            val requestFile: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), image)
        val file :MultipartBody.Part =  MultipartBody.Part.createFormData("image", image.name, requestFile)

        val requestTitle = RequestBody.create(MediaType.parse("multipart/form-data"), title)

        return file?.let { dataClient.addNewPost(requestTitle, it) }
    }
    fun getPosts(): Call<PostsResponse?>? {
        return dataClient.getPosts()
    }
    fun deletePost(data: HashMap<Any?, Any?>?): Call<RootResponse?>? {
        return dataClient.deletePost(data)
    }



}

