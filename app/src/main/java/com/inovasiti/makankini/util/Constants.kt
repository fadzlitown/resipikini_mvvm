package com.inovasiti.makankini.util

class Constants {
    //static constant value

    companion object{
        const val BASE_URL = "https://api.spoonacular.com"
        const val API_KEY = "dc22caa0bba14dd3a1324e77ba400209"

        // API Query Keys
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        //Room DB
        const val DB_NAME = "recipe_database"
        const val RECIPE_TABLE = "recipe_table"

        //Bottom sheet & Pref
        const val DEFAULT_MEAL_NO = "50"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"

        const val PREFERENCES_NAME = "foody_preferences"
        const val PREFERENCES_MEAL_TYPE = "mealType"
        const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCES_DIET_TYPE = "dietType"
        const val PREFERENCES_DIET_TYPE_ID = "dietTypeId"
    }

}