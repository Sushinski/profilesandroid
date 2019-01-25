package ru.profiles.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.profiles.MainActivity
import ru.profiles.ui.LoginFragment
import ru.profiles.ui.SearchFragment
import ru.profiles.ui.SplashFragment
import ru.profiles.ui.registration.ImageEditorFragment
import ru.profiles.ui.registration.RegistrationFragment1
import ru.profiles.ui.registration.RegistrationFragment2

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector(modules = [])
    abstract fun bindLoginFragment() : LoginFragment

    @ContributesAndroidInjector(modules = [])
    abstract fun bindSearchFragment() : SearchFragment

    @ContributesAndroidInjector(modules = [])
    abstract fun bindSplashFragment() : SplashFragment

    @ContributesAndroidInjector(modules = [])
    abstract fun bindReg1Fragment() : RegistrationFragment1

    @ContributesAndroidInjector(modules = [])
    abstract fun bindReg2Fragment() : RegistrationFragment2

    @ContributesAndroidInjector(modules = [])
    abstract fun bindImgEditorFragment() : ImageEditorFragment
}