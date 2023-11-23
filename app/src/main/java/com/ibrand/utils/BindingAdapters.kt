package com.ibrand.utils

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.ibrand.R

// region TextView

@SuppressLint("SetTextI18n")
@BindingAdapter("price", "currency", requireAll = false)
fun TextView.setCurrencyWithPrice(price: String?, currency: String?) {
    val roundedPrice = if (price?.toDoubleOrNull()?.rem(1)?.equals(0.0) == true) { price.substringBefore('.') } else { price?.format("%.2f") }
    text = currency?.let { "$currency $roundedPrice" } ?: "AED $roundedPrice"
}



@SuppressLint("ResourceType")
@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, imageUrl: String?) {
    val drawableCrossFadeFactory =
        DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(false).build()
    val requestListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean, ) = false
        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean, ) = false
    }

    Glide.with(imageView)
        .load(imageUrl)
        .transition(DrawableTransitionOptions.withCrossFade(drawableCrossFadeFactory))
        .placeholder(ColorDrawable(imageView.context.getThemeColor(R.color.colorShimmer)))
        .error(R.drawable.img_error)
        .listener(requestListener)
        .into(imageView)
}