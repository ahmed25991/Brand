package com.ibrand.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtil {

    fun View.hideKeyboard() {
        val inputMethodManager =
            this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive) {
            inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
        }
    }

}