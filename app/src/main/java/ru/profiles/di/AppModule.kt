package ru.profiles.di


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import dagger.Module
import dagger.Provides
import javax.inject.Provider


@Module(includes = [LoginFragmentModule::class])
class AppModule {

    @Provides
    fun provideViewModelFactory(
        providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory =
        AppViewModelFactory(providers)
}