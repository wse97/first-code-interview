package com.mrwaseem.firstcodeinterview.helper

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

abstract class BaseViewModel<N> : ViewModel() {

    private val mIsLoading = ObservableBoolean()

    private var mNavigator: WeakReference<N>? = null

    open fun getIsLoading(): ObservableBoolean? {
        return mIsLoading
    }

    open fun setIsLoading(isLoading: Boolean) {
        mIsLoading.set(isLoading)
    }

    open fun getNavigator(): N? {
        return mNavigator!!.get()
    }

    open fun setNavigator(navigator: N) {
        mNavigator = WeakReference(navigator)
    }

}
