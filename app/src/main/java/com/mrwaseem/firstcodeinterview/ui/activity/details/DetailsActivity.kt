package com.mrwaseem.firstcodeinterview.ui.activity.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.mrwaseem.firstcodeinterview.R
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        Glide.with(this).load(intent.getStringExtra("image")).into(imagePost)
        textPost.text = intent.getStringExtra("title")
    }
}