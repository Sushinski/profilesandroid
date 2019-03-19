package ru.profiles.di

import dagger.Module
import dagger.Provides
import retrofit2.Converter
import ru.profiles.api.builder.*
import ru.profiles.api.interfaces.*
import ru.profiles.profiles.BuildConfig
import javax.inject.Singleton

@Module(includes = [GsonModule::class])
class ApiModule {

    @Singleton
    @Provides
    fun provideAuthApi(converter_factory: Converter.Factory): AuthApi {
        return AuthApiBuilder.buildForUrl(BuildConfig.API_URL, converter_factory)
    }

    @Singleton
    @Provides
    fun providesRegApi(converter_factory: Converter.Factory): RegistrationApi{
        return RegistrationApiBuilder.buildForUrl(BuildConfig.API_URL, converter_factory)
    }

    @Singleton
    @Provides
    fun providesResourcesApi(converter_factory: Converter.Factory): ResourcesApi {
        return ResourcesApiBuilder.buildForUrl(BuildConfig.STORAGE_URL, converter_factory)
    }

    @Provides
    @Singleton
    fun providesServicesApi(converter_factory: Converter.Factory): ServicesApi {
        return ServicesApiBuilder.buildForUrl(BuildConfig.API_URL, converter_factory)
    }
    
}