package com.inovasiti.makankini.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.inovasiti.makankini.R

//These custom layout attribute will be available in the xml
class RecipeCustomBinding {
    companion object {

        @BindingAdapter("loadImage")
        @JvmStatic
        fun loadImage(imageView: ImageView, imageUrl: String) {

            // imageView.load(imageUrl) = is inline function
            // use higher order functions  used inside { ... } ---> inside Builder class there's a funcs
            imageView.load(imageUrl){
                //when is loaded, will have some effects
                crossfade(600)
            }
        }

        @BindingAdapter("setNumberOfLikes")
        @JvmStatic
        fun setNumberOfLikes(textView: TextView, likes: Int) {
            textView.text = likes.toString()
        }

        @BindingAdapter("setNumberOfMins")
        @JvmStatic
        fun setNumberOfMins(textView: TextView, mins: Int) {
            textView.text = mins.toString()
        }

        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view: View, vegan: Boolean) {
            if (vegan) {
                when (view) {
                    is TextView -> {
                        view.setTextColor(ContextCompat.getColor(view.context, R.color.green))
                    }
                    is ImageView -> {
                        view.setColorFilter(ContextCompat.getColor(view.context, R.color.green))
                    }
                }
            }
        }

    }
}