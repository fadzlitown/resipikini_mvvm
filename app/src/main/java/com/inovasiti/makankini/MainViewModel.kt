package com.inovasiti.makankini

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foody.models.FoodRecipe
import com.inovasiti.makankini.data.Repository
import com.inovasiti.makankini.util.NetworkResult
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel @ViewModelInject constructor(
    private val repo: Repository,
    application: BaseApplication
) : AndroidViewModel(application) {

    private var recipesLiveData: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesCall(queries)
    }

    private suspend fun getRecipesCall(queries: Map<String, String>) {
        recipesLiveData.value = NetworkResult.Loading()

        if (hasInternet()) {
            try {
                val response = repo.remote.getRecipes(queries)
                recipesLiveData.value = handleResponse(response)
            } catch (e: Exception) {
                recipesLiveData.value = NetworkResult.Error("Recipe Not Found")
            }

        } else {
            recipesLiveData.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun handleResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API usage exceeded")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Not found")
            }

            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternet(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}