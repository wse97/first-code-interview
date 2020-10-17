package com.mrwaseem.firstcodeinterview.ui.activity.AddPost

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.asksira.bsimagepicker.BSImagePicker
import com.bumptech.glide.Glide
import com.mrwaseem.firstcodeinterview.R
import com.mrwaseem.firstcodeinterview.databinding.ActivityAddPostBinding
import com.ugb.findup.Components.LoadeDialog
import com.ugb.findup.Helpar.PermissionsManager
import com.ugb.findup.Helpar.RealPathUtil
import kotlinx.android.synthetic.main.item_posts.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class AddPostActivity : AppCompatActivity(),NavigatorAddPost ,
    BSImagePicker.OnSingleImageSelectedListener,

    BSImagePicker.ImageLoaderDelegate {
    var file :File?=null
    var p = ArrayList<String>()
    lateinit var dataBinding : ActivityAddPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel:AddPostViewModel = ViewModelProvider(this).get(AddPostViewModel::class.java)
        viewModel.setNavigator(this)
        LoadeDialog.InstanceDialog(this)
        RealPathUtil.getInstance(this)
        p.add("android.permission.WRITE_EXTERNAL_STORAGE")
        p.add(Manifest.permission.CAMERA)

        dataBinding = DataBindingUtil.setContentView(this,R.layout.activity_add_post)

        dataBinding.btnAddPost.setOnClickListener {
            if (file!=null){
                if (!dataBinding.eTextTitle.text.isNullOrEmpty()){
                    LoadeDialog.showDialog()
                    viewModel.addPost(dataBinding.eTextTitle.text.toString(), file!!)
                }
                else{
                    dataBinding.eTextTitle.error = "الحقل مطلوب"
                }
            }
        }
        dataBinding.btnUploadImage.setOnClickListener {
            if (PermissionsManager.getInstance(this).checkPermission(p[0])&&
                PermissionsManager.getInstance(this).checkPermission(p[1])){
                RealPathUtil.GoToImage(supportFragmentManager,this.packageName)
            }
        }
    }

    override fun handleError(e: Exception?) {
        Log.d("Error",e?.message)
        LoadeDialog.dismissDialog()
    }

    override fun isPostAdded() {
        LoadeDialog.dismissDialog()
        finish()
    }

    override fun onResponseError(message :String?) {
        Log.d("ResponseError",message)
        LoadeDialog.dismissDialog()
    }

    override fun onSingleImageSelected(uri: Uri?, tag: String?) {
        file = File(RealPathUtil.getRealPath(uri))
        Glide.with(this).load(uri).into(dataBinding.imagePost)

    }

    override fun loadImage(imageUri: Uri?, ivImage: ImageView?) {
        Glide.with(this).load(imageUri).into(ivImage!!)
    }

    override fun onRequestPermissionsResult(permsRequestCode: Int,
                                            permissions: Array<String?>, grantResults: IntArray) {
        val stringBuilder = StringBuilder()
        val str = "onRequestPermissionsResult: "
        stringBuilder.append(str)
        stringBuilder.append(permissions)
        if (permsRequestCode == 124 && grantResults.isNotEmpty()) {
            RealPathUtil.GoToImage(supportFragmentManager, this.packageName)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return
        if (requestCode == RealPathUtil.requestCodeCameraImage) {
            if (data != null) {
                val photo = (Objects.requireNonNull(data.extras)?.get("data") as Bitmap?)!!
                val tempUri: Uri? = getImageUri( photo, "imagePost::" +
                        Calendar.getInstance().time.time)
                Log.d("tempUri",tempUri.toString())
                val path = RealPathUtil.getRealPath(tempUri)
                file = File(path)
                Glide.with(this).load(tempUri).into(dataBinding.imagePost)

            }
            return
        }

    }

    fun getImageUri(inImage: Bitmap, name: String?): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, inImage, name, null)
        return Uri.parse(path)
    }
}