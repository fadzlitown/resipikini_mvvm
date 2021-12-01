package com.inovasiti.makankini.di

import com.inovasiti.makankini.util.Constants
import com.inovasiti.makankini.data.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
//InstallIn containers (SingletonComponent / ApplicationComponent  in the code) module bindings must be available.
//object = a special class that only has one instance
object NetworkModule {

    //@Provide = add all the dependencies by providing a method, which represented on constructors

    @Provides
    fun provideHttpClient(): OkHttpClient{
        return OkHttpClient.Builder()
            .readTimeout(15,TimeUnit.SECONDS)
            .connectTimeout(15,TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory{
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideApiService(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(ApiService::class.java)    }

}