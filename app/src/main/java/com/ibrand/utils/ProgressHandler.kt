package com.ibrand.utils

import android.app.ProgressDialog
import android.content.Context

class ProgressHandler {
    var progressDialog: ProgressDialog? = null
    fun showProgress(context: Context?, message: String?) {
        if (progressDialog != null && progressDialog!!.isShowing) {
            hideProgress()
        }
        progressDialog = ProgressDialog(context)
        progressDialog?.setMessage(message)
        progressDialog?.setCancelable(false)
        progressDialog?.setCanceledOnTouchOutside(false)
        progressDialog?.show()
    }

    fun hideProgress() {
        if (progressDialog != null) progressDialog!!.dismiss()
    }

    fun handleProgress(isLoading: Boolean = false, context: Context?, message: String) {
        when (isLoading) {
            true -> showProgress(context, message)
            false -> hideProgress()
        }
    }
}