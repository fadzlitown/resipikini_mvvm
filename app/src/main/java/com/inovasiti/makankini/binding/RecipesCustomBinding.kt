package com.inovasiti.makankini.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.inovasiti.makankini.data.database.RecipeEntity
import com.inovasiti.makankini.model.FoodRecipe
import com.inovasiti.makankini.util.NetworkResult

class RecipesCustomBinding {

    companion object{
        @BindingAdapter("readAPI", "readDB", requireAll = true)
        @JvmStatic
        fun setErrorImageView(imageView: ImageView, apiResponse: NetworkResult<FoodRecipe>?, db :List<RecipeEntity>?){
            if(apiResponse is NetworkResult.Error && db.isNullOrEmpty()){
                imageView.visibility = View.VISIBLE
            } else if(apiResponse is NetworkResult.Loading || apiResponse is NetworkResult.Success){
                imageView.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("readErrorAPI", "readErrorDB", requireAll = true)
        @JvmStatic
        fun setErrorText(textView: TextView, apiResponse: NetworkResult<FoodRecipe>?, db :List<RecipeEntity>?){
            if(apiResponse is NetworkResult.Error && db.isNullOrEmpty()){
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            } else if(apiResponse is NetworkResult.Loading || apiResponse is NetworkResult.Success){
                textView.visibility = View.INVISIBLE
            }
        }
    }
}