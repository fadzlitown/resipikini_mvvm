package com.inovasiti.makankini.di

import android.content.Context
import androidx.room.Room.databaseBuilder
import com.inovasiti.makankini.data.database.RecipeDao
import com.inovasiti.makankini.data.database.RecipesDatabase
import com.inovasiti.makankini.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): RecipesDatabase {
        //RecipesDatabase used abstract db class
        return databaseBuilder(context, RecipesDatabase::class.java, Constants.DB_NAME).build()
    }


    @Singleton
    @Provides
    fun provideDao(database: RecipesDatabase): RecipeDao {
        // was called in RecipesDatabase
        return database.recipesDao()
    }

}