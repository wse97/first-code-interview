package com.ugb.findup.Helpar

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionsManager(private val context: Activity) : AppCompatActivity() {
    private val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124
    fun checkPermission(prmi: ArrayList<String>) {
        val re = arrayOfNulls<String>(prmi.size)
        var pos = 0
        for (i in prmi.indices) {
            if (ContextCompat.checkSelfPermission(context, prmi[i]!!) != 0) {
                re[pos] = prmi[i]
                pos++
            }
        }
        if (re.isNotEmpty())
        requestPermission(re)
    }
    fun checkPermission(p: String?): Boolean {
        if (ContextCompat.checkSelfPermission(context, p!!) != 0) {
            requestPermission(arrayOf(p))
            return false
        }
        return true
    }

    private fun requestPermission(r: Array<String?>) {

        ActivityCompat.requestPermissions(context, r, 124)
    }

    override fun onRequestPermissionsResult(permsRequestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val stringBuilder = StringBuilder()
        val str = "onRequestPermissionsResult: "
        stringBuilder.append(str)
        stringBuilder.append(permissions)
        val stringBuilder2 = stringBuilder.toString()
        val str2 = "TESATT"
        Log.d(str2, stringBuilder2)
        if (permsRequestCode == 124 && grantResults.size > 0) {
            var locationAccepted = false
            if (grantResults[0] == 0) {
                locationAccepted = true
            }
            Log.d(str2, str)
            if (!locationAccepted) {
                context.finish()
            }
        }
    }

    companion object {
        fun getInstance(context: Activity): PermissionsManager {
            return PermissionsManager(context)
        }
    }

}