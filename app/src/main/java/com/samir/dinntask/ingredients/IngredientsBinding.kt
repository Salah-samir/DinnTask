package com.samir.dinntask.ingredients

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter(value = ["imageUri", "placeholder"], requireAll = false)
fun ImageView.showImageWithUri(imageUri: String?, placeholder: Drawable? = null) {
    when (imageUri) {
        null -> {
            Glide.with(this)
                .load(placeholder)
                .into(this)
        }
        else -> {

            Glide.with(this)
                .load(imageUri)
                .into(this)
        }
    }
}

@BindingAdapter("invisibleUnless")
fun invisibleUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("goneUnless")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}
