package com.mrwaseem.firstcodeinterview

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mrwaseem.firstcodeinterview.ui.activity.AddPost.AddPostActivity
import com.mrwaseem.firstcodeinterview.ui.fragment.PostsFragment
import com.ugb.findup.Components.LoadeDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LoadeDialog.InstanceDialog(this)

        var fragment = PostsFragment()
        replaceFragment(fragment)
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->

            var intent = Intent(this, AddPostActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onResume() {
        LoadeDialog.InstanceDialog(this)
        super.onResume()
    }
    private fun replaceFragment(fragment: Fragment?) {
        supportFragmentManager.beginTransaction().replace(R.id.content, fragment!!)
            .commit()
    }
}
