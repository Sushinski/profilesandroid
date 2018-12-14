package ru.profiles.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class GsonModule {

    @Singleton
    @Provides
    fun provideGsonConverter() : Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(gson: Gson): Converter.Factory{
        return GsonConverterFactory.create(gson)
    }

}