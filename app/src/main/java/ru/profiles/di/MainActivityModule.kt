package ru.profiles.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.profiles.MainActivity
import ru.profiles.interfaces.MainActivityOps
import ru.profiles.ui.LoginFragment

@Module
abstract class MainActivityModule {

    @Binds
    abstract fun provideMainActivity(mainActivity: MainActivity): MainActivityOps
}