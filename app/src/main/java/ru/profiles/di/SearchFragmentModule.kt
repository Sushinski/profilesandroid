package ru.profiles.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import ru.profiles.data.AuthRepository
import ru.profiles.data.UserRepository
import ru.profiles.ui.SearchFragment
import ru.profiles.viewmodel.SearchViewModel


@Module(includes=[SearchFragmentModule.ProvideViewModelModule::class])
abstract class SearchFragmentModule {

    @ContributesAndroidInjector(modules = [InjectViewModelModule::class])
    abstract fun bind(): SearchFragment

    @Module
    class ProvideViewModelModule {

        @Provides
        @IntoMap
        @ViewModelKey(SearchViewModel::class)
        fun provideSearchViewModel(authRep: AuthRepository, userRep: UserRepository, gson: Gson): ViewModel =
            SearchViewModel()
    }

    @Module
    class InjectViewModelModule {

        @Provides
        fun provideSearchViewModel(factory: ViewModelProvider.Factory, target: SearchFragment): SearchViewModel =
            ViewModelProviders.of(target, factory).get(SearchViewModel::class.java)
    }

}