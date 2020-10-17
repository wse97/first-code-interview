package com.mrwaseem.firstcodeinterview.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mr.waseem.firstcode.Data.Model.PostsModel
import com.mrwaseem.firstcodeinterview.R
import com.mrwaseem.firstcodeinterview.data.local.DBManager
import com.mrwaseem.firstcodeinterview.data.local.PostsDatabase
import com.mrwaseem.firstcodeinterview.databinding.FragmentPostsBinding
import com.mrwaseem.firstcodeinterview.ui.adapter.PostsAdapter
import com.ugb.findup.Components.LoadeDialog
import java.lang.Exception


class PostsFragment : Fragment(), NavigatorPosts {

    lateinit var adapterPosts: PostsAdapter
    lateinit var viewModel: PostsViewModel

    private var map: HashMap<Any?, Any?> = hashMapOf()
    var dbManagar: PostsDatabase? = null

    @SuppressLint("ShowToast")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPostsBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_posts, container, false
        )


        binding.recyclerPosts.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )

        dbManagar = context?.applicationContext?.let { DBManager().getDatabase(it) }


        adapterPosts = context?.let { context ->
            dbManagar?.postsDao()?.getPosts()?.let {
                PostsAdapter(context, it) { pos ->
                    LoadeDialog.showDialog()
                    map["id"] = dbManagar?.postsDao()?.getPosts()!![pos].id
                    viewModel.deletePost(map, pos)

                }
            }
        }!!
        binding.recyclerPosts.adapter = adapterPosts

        viewModel = ViewModelProvider(this).get(PostsViewModel::class.java)
        viewModel.setNavigator(this)

        activity?.let {
            viewModel.postsMutableLiveData.observe(it) { data ->
                dbManagar?.postsDao()?.deleteAll()
                dbManagar?.postsDao()?.insertPost(data.posts)
                dbManagar?.postsDao()?.getPosts()?.let { it1 -> adapterPosts.setData(it1) }
                adapterPosts.notifyDataSetChanged()
                LoadeDialog.dismissDialog()

            }
        }

        return binding.root
    }

    override fun handleErrorGetPosts(e: Exception?) {
        LoadeDialog.dismissDialog()
    }

    override fun handleErrorDeletePost(e: Exception?) {
        Log.d("ErrorDeletePost", e?.message)
        LoadeDialog.dismissDialog()
    }

    override fun isPostDelete(pos: Int) {
        Log.d("isDeleted", "isDeleted")
        viewModel.postsMutableLiveData.value?.posts?.get(pos)?.id?.let {
            dbManagar?.postsDao()?.deleteFormPostId(it)
        }
        viewModel.postsMutableLiveData.value?.posts?.removeAt(pos)

        dbManagar?.postsDao()?.getPosts()?.let { adapterPosts.setData(it) }
        adapterPosts.notifyDataSetChanged()
        LoadeDialog.dismissDialog()

    }

    override fun onResponseError(message: String) {
        Log.d("ErrorMsg", message)
        LoadeDialog.dismissDialog()
    }


    override fun onResume() {
        LoadeDialog.showDialog()
        viewModel.getPosts()
        super.onResume()
    }

}