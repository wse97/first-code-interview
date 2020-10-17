package com.ugb.findup.Helpar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.loader.content.CursorLoader
import com.asksira.bsimagepicker.BSImagePicker
import com.asksira.bsimagepicker.Utils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations
import com.mrwaseem.firstcodeinterview.R
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.camera_or_gallary.*
import kotlinx.android.synthetic.main.camera_or_gallary.view.*
import java.io.File
import java.util.*

class RealPathUtil(context: Activity) {
    companion object {
        private var activity: Activity? = null
        private var realPathUtil: RealPathUtil? = null
        var requestCodeVideo = 50
        var requestCodeImage = 45
        var requestCodeCameraImage = 46
        var selectedUriList: List<Uri> = ArrayList()
        fun getInstance(context: Activity) {
            realPathUtil = RealPathUtil(context)
        }

        fun GoToVideo() {
//        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        context.startActivityForResult(pickIntent, requestCodeVideo);
            val intent = Intent(activity, FilePickerActivity::class.java)
            intent.putExtra(FilePickerActivity.CONFIGS, Configurations.Builder()
                    .setCheckPermission(true)
                    .setShowImages(false)
                    .setShowVideos(true)
                    .enableImageCapture(false)
                    .setSingleClickSelection(true)
                    .setMaxSelection(1)
                    .setSkipZeroSizeFiles(true)
                    .build())
            activity!!.startActivityForResult(intent, requestCodeVideo)
        }

        fun cropImage(activity: Activity, sourceUri: Uri?) {
            val destinationUri = Uri.fromFile(File(activity.cacheDir, queryName(activity.contentResolver, sourceUri)))
            val options = UCrop.Options()
            options.setCompressionQuality(80)
            options.setFreeStyleCropEnabled(true)
            options.setToolbarColor(ContextCompat.getColor(activity, R.color.white))
            options.setStatusBarColor(ContextCompat.getColor(activity, R.color.white))
            options.setActiveControlsWidgetColor(ContextCompat.getColor(activity, R.color.purple_700))
            UCrop.of(sourceUri!!, destinationUri)
                    .withOptions(options)
                    .start(activity)
        }

        fun queryName(resolver: ContentResolver, uri: Uri?): String {
            val returnCursor = resolver.query(uri!!, null, null, null, null)!!
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            val name = returnCursor.getString(nameIndex)
            returnCursor.close()
            return name
        }

        private fun showDialogImage(manager: FragmentManager, activityPackage: String, tag: String = "request") {
            val dialog = BottomSheetDialog(activity!!, R.style.CustomBottomSheetDialogTheme)
            val binding = LayoutInflater.from(activity).inflate(R.layout.camera_or_gallary, null)
            dialog.setContentView(binding)
            binding.gallary.setOnClickListener {
                dialog.dismiss()
                val singleSelectionPicker = BSImagePicker.Builder(activityPackage)
                        .setSpanCount(3)
                        .setGridSpacing(Utils.dp2px(2))
                        .setPeekHeight(Utils.dp2px(360))
                        .hideCameraTile()
                        .hideGalleryTile()
                        .setTag(tag)
                        .build()
                singleSelectionPicker.show(manager, tag)
            }
            dialog.camera.setOnClickListener {
                dialog.dismiss()
                val m_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                activity!!.startActivityForResult(m_intent, requestCodeCameraImage)
            }
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            dialog.behavior.peekHeight = 0
            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            dialog.behavior.addBottomSheetCallback(
                    object : BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            if (newState == 0) {
                                dialog.dismiss()
                            }
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                    })
            dialog.show()

        }

        fun GoToImage(manager: FragmentManager, activityPackage: String, tag: String = "tag") {
            Log.d("EWEWEWERWER", "GoToImage: " + activityPackage)
            showDialogImage(manager, activityPackage, tag)
        }

        fun getRealPath(data: Intent): String? {
            val realPath: String?
            realPath = if (Build.VERSION.SDK_INT < 11) {
                getRealPathFromURI_BelowAPI11(activity!!, data.data)
            } else if (Build.VERSION.SDK_INT < 19) {
                getRealPathFromURI_API11to18(activity!!, data.data)
            } else {
                getRealPathFromURI_API19(activity!!, data.data)
            }
            return realPath
        }

        fun getRealPath(data: Uri?): String? {
            val realPath: String?
            realPath = if (Build.VERSION.SDK_INT < 11) {
                getRealPathFromURI_BelowAPI11(activity!!, data)
            } else if (Build.VERSION.SDK_INT < 19) {
                getRealPathFromURI_API11to18(activity!!, data)
            } else {
                getRealPathFromURI_API19(activity!!, data)
            }
            return realPath
        }

        @SuppressLint("NewApi")
        private fun getRealPathFromURI_API11to18(context: Context, contentUri: Uri?): String? {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            var result: String? = null
            val cursorLoader = CursorLoader(context, contentUri!!, proj, null, null, null)
            val cursor = cursorLoader.loadInBackground()
            if (cursor != null) {
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                result = cursor.getString(column_index)
                cursor.close()
            }
            return result
        }

        private fun getRealPathFromURI_BelowAPI11(context: Context, contentUri: Uri?): String {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            var column_index = 0
            var result = ""
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                result = cursor.getString(column_index)
                cursor.close()
                return result
            }
            return result
        }

        /**
         * Get a file path from a Uri. This will get the the path for Storage Access
         * Framework Documents, as well as the _data field for the MediaStore and
         * other file-based ContentProviders.
         *
         * @param context The context.
         * @param uri     The Uri to query.
         * @author paulburke
         */
        @SuppressLint("NewApi")
        private fun getRealPathFromURI_API19(context: Context, uri: Uri?): String? {
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(
                            split[1]
                    )
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }
            } else if ("content".equals(uri!!.scheme, ignoreCase = true)) {

                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }
            return null
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context       The context.
         * @param uri           The Uri to query.
         * @param selection     (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        private fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                                  selectionArgs: Array<String>?): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(
                    column
            )
            try {
                cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs,
                        null)
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        private fun isExternalStorageDocument(uri: Uri?): Boolean {
            return "com.android.externalstorage.documents" == uri!!.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        private fun isDownloadsDocument(uri: Uri?): Boolean {
            return "com.android.providers.downloads.documents" == uri!!.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        private fun isMediaDocument(uri: Uri?): Boolean {
            return "com.android.providers.media.documents" == uri!!.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        private fun isGooglePhotosUri(uri: Uri?): Boolean {
            return "com.google.android.apps.photos.content" == uri!!.authority
        }
    }

    init {
        activity = context
    }
}