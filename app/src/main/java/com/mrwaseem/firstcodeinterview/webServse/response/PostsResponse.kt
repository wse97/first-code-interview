package com.mrwaseem.firstcodeinterview.webServse.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.mr.waseem.firstcode.Data.Model.PostsModel
import java.util.*

class PostsResponse : RootResponse() {

    @SerializedName("data")
    @Expose
    var posts: ArrayList<PostsModel> = ArrayList()

}