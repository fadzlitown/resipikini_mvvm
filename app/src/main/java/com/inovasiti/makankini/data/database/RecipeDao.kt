package com.inovasiti.makankini.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    //replace strategy only if there's a new data from the API
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(recipeEntity: RecipeEntity)

    @Query("SELECT * FROM recipe_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipeEntity>> //will work with livedata
}