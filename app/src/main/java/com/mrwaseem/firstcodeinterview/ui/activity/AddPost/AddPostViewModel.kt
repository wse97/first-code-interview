package com.mrwaseem.firstcodeinterview.ui.activity.AddPost

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mrwaseem.firstcodeinterview.helper.BaseViewModel
import com.mrwaseem.firstcodeinterview.webServse.response.AddPostResponse
import com.ugb.mvvmkotlin.ApiClient.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import java.io.File

class AddPostViewModel : BaseViewModel<NavigatorAddPost>() {

    fun addPost(title: String, image: File) {
        setIsLoading(true)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.getINSTANCE().addNewPost(title, image)?.awaitResponse()
                withContext(Dispatchers.Main) {
                    if (response?.isSuccessful!!) {
                        getNavigator()?.isPostAdded()
                    } else {
                        getNavigator()?.onResponseError(response.message())
                    }
                    setIsLoading(false)
                }
            } catch (e: Exception) {
                setIsLoading(false)
                getNavigator()?.handleError(e)
            }


        }
    }

}