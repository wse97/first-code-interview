package com.mrwaseem.firstcodeinterview.ui.activity.AddPost

interface NavigatorAddPost {
    fun handleError(e: Exception?)

    fun isPostAdded()

    fun onResponseError(message :String?)

}