package com.inovasiti.makankini.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.inovasiti.makankini.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.inovasiti.makankini.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.inovasiti.makankini.util.Constants.Companion.PREFERENCES_DIET_TYPE
import com.inovasiti.makankini.util.Constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.inovasiti.makankini.util.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.inovasiti.makankini.util.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


val Context.dataStore by preferencesDataStore("foody_preferences")

//replace SharedPreference

@ActivityRetainedScoped //used this ActivityRetainedScoped bcs this repo will be use in our RecipeViewModel
class RecipeDataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private val dataStore = context.dataStore

    //Need to keep these setting filter in pref
    private object PreferenceKeys {
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCES_DIET_TYPE_ID)
    }

    //save pref
    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        dataStore.edit { pref ->
            pref[PreferenceKeys.selectedMealType] = mealType
            pref[PreferenceKeys.selectedMealTypeId] = mealTypeId
            pref[PreferenceKeys.selectedDietType] = dietType
            pref[PreferenceKeys.selectedDietTypeId] = dietTypeId
        }
    }

    //read datastore pref that we save before
    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        .catch { exp ->
            if (exp is IOException) {
                emit(emptyPreferences())
            } else {
                throw  exp
            }
        }.map { pref ->
            /// NOTED expression ?: means there's no value then = DEFAULT_MEAL_TYPE otherwise = pref[PreferenceKeys.selectedMealType]
            val selectedMealType = pref[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = pref[PreferenceKeys.selectedMealTypeId] ?: 0
            val selectedDietType = pref[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = pref[PreferenceKeys.selectedDietTypeId] ?: 0
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }
}


// Need model to represent our filter business logic
// store string & typeId
data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)