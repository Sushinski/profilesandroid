package ru.profiles.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import ru.profiles.data.AuthRepository
import ru.profiles.data.UserRepository
import ru.profiles.ui.LoginFragment
import ru.profiles.viewmodel.LoginViewModel


@Module(includes=[LoginFragmentModule.ProvideViewModelModule::class])
abstract class LoginFragmentModule {

    @ContributesAndroidInjector(modules = [InjectViewModelModule::class])
    abstract fun bind(): LoginFragment

    @Module
    class ProvideViewModelModule {

        @Provides
        @IntoMap
        @ViewModelKey(LoginViewModel::class)
        fun provideLoginViewModel(authRep: AuthRepository, userRep: UserRepository): ViewModel =
            LoginViewModel(userRep, authRep)
    }

    @Module
    class InjectViewModelModule {

        @Provides
        fun provideLoginViewModel(factory: ViewModelProvider.Factory, target: LoginFragment): LoginViewModel =
            ViewModelProviders.of(target, factory).get(LoginViewModel::class.java)
    }

}