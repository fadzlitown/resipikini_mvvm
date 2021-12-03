package com.inovasiti.makankini.data

import com.inovasiti.makankini.data.database.RecipeDao
import com.inovasiti.makankini.data.database.RecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipeDao: RecipeDao    //RecipeDao created in DI module
) {

    fun readDatabase(): Flow<List<RecipeEntity>> {
        return recipeDao.readRecipes()
    }

    fun insertRecipes(recipe: RecipeEntity){
        recipeDao.insertRecipes(recipe)
    }

}