package com.inovasiti.makankini.data

import com.inovasiti.makankini.data.network.ApiService
import com.inovasiti.makankini.model.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getRecipes(queries: Map<String,String>) : Response<FoodRecipe> {
        return apiService.getRecipes(queries)
    }
}