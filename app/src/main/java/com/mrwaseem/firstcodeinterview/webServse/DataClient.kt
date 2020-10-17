package com.ugb.mvvmkotlin.ApiClient

import com.mrwaseem.firstcodeinterview.webServse.response.RootResponse
import com.mrwaseem.firstcodeinterview.webServse.response.AddPostResponse
import com.mrwaseem.firstcodeinterview.webServse.response.PostsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.HashMap


interface DataClient {

    @Multipart
    @POST("posts")
    fun addNewPost(@Part("title") title: RequestBody,
                    @Part image: MultipartBody.Part): Call<AddPostResponse?>?

    @GET("posts")
    fun getPosts(): Call<PostsResponse?>?

    @POST("posts/delete")
    fun deletePost(@Body data: HashMap<Any?, Any?>?): Call<RootResponse?>?


}