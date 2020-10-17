package com.ugb.findup.Components

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.mrwaseem.firstcodeinterview.R

class LoadeDialog {
    companion object {
        var dialog: Dialog? = null
        fun InstanceDialog(context: Context) {
            dialog = Dialog(context)
            dialog?.setContentView(R.layout.my_loder)
            dialog?.setCancelable(false)
            if (dialog?.window != null) {
                dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog?.window!!.transitionBackgroundFadeDuration = 200
                dialog?.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }
        }

        fun showDialog() {
            if (dialog != null) {
                if (!dialog!!.isShowing)
                    dialog!!.show()
            }
        }

        fun dismissDialog() {
            if (dialog != null)
                if (dialog!!.isShowing) dialog!!.dismiss()
        }
    }
}