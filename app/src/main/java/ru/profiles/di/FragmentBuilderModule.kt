package ru.profiles.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.profiles.MainActivity
import ru.profiles.ui.LoginFragment
import ru.profiles.ui.SearchFragment
import ru.profiles.ui.SplashFragment

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector(modules = [])
    abstract fun bindLoginFragment() : LoginFragment

    @ContributesAndroidInjector(modules = [])
    abstract fun bindSearchFragment() : SearchFragment

    @ContributesAndroidInjector(modules = [])
    abstract fun bindSplashFragment() : SplashFragment
}