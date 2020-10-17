package com.mrwaseem.firstcodeinterview.ui.fragment

import java.lang.Exception

interface NavigatorPosts {

    fun handleErrorGetPosts(e: Exception?)

    fun handleErrorDeletePost(e: Exception?)

    fun isPostDelete(pos:Int)

    fun onResponseError(message : String)
}