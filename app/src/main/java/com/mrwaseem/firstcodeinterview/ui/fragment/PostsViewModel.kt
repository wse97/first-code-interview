package com.mrwaseem.firstcodeinterview.ui.fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mrwaseem.firstcodeinterview.helper.BaseViewModel
import com.mrwaseem.firstcodeinterview.webServse.response.PostsResponse
import com.ugb.mvvmkotlin.ApiClient.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import java.util.*

class PostsViewModel : BaseViewModel<NavigatorPosts>() {

    var postsMutableLiveData= MutableLiveData<PostsResponse>()

    fun getPosts() {
        setIsLoading(true)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.getINSTANCE().getPosts()?.awaitResponse()
                withContext(Dispatchers.Main) {
                    if (response?.isSuccessful!!) {
                        postsMutableLiveData.value = response.body()

                    } else {
                        getNavigator()?.onResponseError(response.message())

                    }
                    setIsLoading(false)
                }
            } catch (e: Exception) {
                setIsLoading(false)
                getNavigator()?.handleErrorGetPosts(e)
            }


        }
    }

    fun deletePost(data: HashMap<Any?, Any?>?, pos: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.getINSTANCE().deletePost(data)?.awaitResponse()
                withContext(Dispatchers.Main) {
                    if (response?.isSuccessful!!) {
                        getNavigator()?.isPostDelete(pos)
                    } else {
                        getNavigator()?.onResponseError(response.message())
                    }
                }
            } catch (e: Exception) {
                getNavigator()?.handleErrorDeletePost(e)
            }
        }
    }

}