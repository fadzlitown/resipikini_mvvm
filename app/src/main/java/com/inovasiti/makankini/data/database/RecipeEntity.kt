package com.inovasiti.makankini.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.inovasiti.makankini.model.FoodRecipe
import com.inovasiti.makankini.util.Constants.Companion.RECIPE_TABLE

@Entity(tableName = RECIPE_TABLE)
class RecipeEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)   //only fetch the new data, only 0 always
    var id: Int = 0
}