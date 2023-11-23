package com.ibrand.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.ibrand.R
import com.ibrand.databinding.CustomToastBinding

import kotlinx.coroutines.delay
import java.util.*
import java.util.concurrent.TimeUnit


object ToastUtil {

    private val duration = TimeUnit.SECONDS.toMillis(3)

    suspend fun Context.showToast(
        message: String,
        iconToast: Int? = null,
        cancelableToast: Boolean? = null,
        onFinish: () -> Unit
    ) {
        val icon = iconToast ?: R.drawable.img_error
        val cancelable = cancelableToast ?: true

        val binding: CustomToastBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.custom_toast,
            null,
            false
        )

        binding.contentTV.text = message
        binding.contentTV.setCompoundDrawablesWithIntrinsicBounds(0, icon, 0, 0)

        Dialog(this, R.style.dialogTheme).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            setCanceledOnTouchOutside(cancelable)
            setCancelable(cancelable)
            window?.apply {
                val width = (this@showToast.resources.displayMetrics.widthPixels * 0.9).toInt()
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
            show()
            delay(duration)
            this.dismiss()
            onFinish()
        }

    }


}