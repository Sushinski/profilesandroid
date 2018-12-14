package ru.profiles.di

import dagger.Module

import dagger.android.ContributesAndroidInjector
import ru.profiles.MainActivity


@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity() : MainActivity
}