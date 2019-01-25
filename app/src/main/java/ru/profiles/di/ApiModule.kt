package ru.profiles.di

import dagger.Module
import dagger.Provides
import retrofit2.Converter
import ru.profiles.api.builder.AuthApiBuilder
import ru.profiles.api.builder.RegistrationApiBuilder
import ru.profiles.api.interfaces.AuthApi
import ru.profiles.api.interfaces.RegistrationApi
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

}