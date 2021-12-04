package com.inovasiti.makankini

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.inovasiti.makankini.data.RecipeDataStoreRepository
import com.inovasiti.makankini.util.Constants
import com.inovasiti.makankini.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.inovasiti.makankini.util.Constants.Companion.DEFAULT_MEAL_TYPE
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecipeViewModel @ViewModelInject constructor(application: Application, val dataStoreRepo: RecipeDataStoreRepository)
    : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    val readMealAndDietType = dataStoreRepo.readMealAndDietType

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) {
        viewModelScope.launch {
            dataStoreRepo.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }
        queries[Constants.QUERY_NUMBER] = Constants.DEFAULT_MEAL_NO
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_TYPE] = mealType    //will apply from default constant OR selected type on filter by user
        queries[Constants.QUERY_DIET] = dietType
        queries[Constants.QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }

}