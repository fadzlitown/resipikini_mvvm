package com.inovasiti.makankini

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.inovasiti.makankini.databinding.RecipesRowLayoutBinding
import com.inovasiti.makankini.model.FoodRecipe
import com.inovasiti.makankini.model.Result
import com.inovasiti.makankini.util.RecipeResultDiffUtil

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private var recipes = emptyList<Result>()

    class RecipeViewHolder(private val binding: RecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result) {
            binding.result = result
            binding.executePendingBindings()    //updating any views that modified variables, run in ui thread
        }

        companion object {
            fun from(parent: ViewGroup): RecipeViewHolder {
                val binding = RecipesRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return RecipeViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentRecipe = recipes[position]
        holder.bind(currentRecipe)
    }

    fun setRecipe(newRecipe: FoodRecipe){
        val diffRecipes = RecipeResultDiffUtil(recipes, newRecipe.results)
        val diffResult = DiffUtil.calculateDiff(diffRecipes)
        recipes = newRecipe.results
        diffResult.dispatchUpdatesTo(this)
//        notifyDataSetChanged()  //Note : notify rv adapter = but overkill coz update the whole data,
    //        SHOULD used DiffUtil like above = only update the view that are not the same

    }


}