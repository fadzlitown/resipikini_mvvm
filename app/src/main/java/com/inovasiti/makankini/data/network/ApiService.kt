package com.inovasiti.makankini.data.network

import com.inovasiti.makankini.model.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(@QueryMap queries: Map<String, String>) : Response<FoodRecipe>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipe(@QueryMap searchQueryMap: Map<String,String>) : Response<FoodRecipe>

}