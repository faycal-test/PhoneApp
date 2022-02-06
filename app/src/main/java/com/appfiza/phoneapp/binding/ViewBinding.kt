package com.appfiza.phoneapp.binding

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.appfiza.phoneapp.model.Device
import com.appfiza.phoneapp.util.gone
import com.appfiza.phoneapp.util.hide
import com.appfiza.phoneapp.util.visible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.facebook.shimmer.ShimmerFrameLayout

/**
 * Created by Fayçal KADDOURI 🐈
 */
object ViewBinding {

    @JvmStatic
    @BindingAdapter("showError")
    fun showError(view: View, error: Boolean) {
        if (error) {
            view.visible()
        } else {
            view.gone()
        }
    }

    @JvmStatic
    @BindingAdapter("favoriteIcon")
    fun favoriteIcon(imageView: ImageView, device: Device?) {
        device?.let {
            if (it.isFavorite) imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    imageView.context,
                    android.R.drawable.btn_star_big_on
                )
            )
            else imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    imageView.context,
                    android.R.drawable.star_off
                )
            )
        }
    }


    @JvmStatic
    @BindingAdapter("shimmerEffectLoading")
    fun shimmerEffectLoading(view: ShimmerFrameLayout, enable: Boolean) {
        if (enable) {
            view.visible()
            view.startShimmer()
        } else {
            view.hide()
            view.stopShimmer()
        }
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImageUrl(view: ImageView, url: String?) {
        val requestOptions = RequestOptions()
            .placeholder(android.R.color.darker_gray)
            .transform(CenterCrop())
        Glide.with(view.context)
            .applyDefaultRequestOptions(requestOptions)
            .load(url).into(view)
    }
}