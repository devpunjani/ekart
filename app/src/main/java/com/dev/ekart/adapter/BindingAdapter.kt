package com.dev.ekart.adapter

import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dev.ekart.R


@BindingAdapter("loadImageURL")
fun setImage(iv: ImageView, url: String?){
    Glide.with(iv.context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .centerCrop()
        .into(iv)
}


@BindingAdapter("loadImageURLCircle")
fun setImageCircle(iv: ImageView, url: String?){
    Glide.with(iv.context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .circleCrop()
        .into(iv)
}

