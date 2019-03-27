package ru.profiles.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.profiles.ui.*
import ru.profiles.ui.registration.ImageEditorFragment
import ru.profiles.ui.registration.RegistrationFragment1
import ru.profiles.ui.registration.RegistrationFragment2

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector(modules = [])
    abstract fun bindLoginFragment() : LoginFragment

    @ContributesAndroidInjector(modules = [])
    abstract fun bindSplashFragment() : SplashFragment

    @ContributesAndroidInjector(modules = [])
    abstract fun bindReg1Fragment() : RegistrationFragment1

    @ContributesAndroidInjector(modules = [])
    abstract fun bindReg2Fragment() : RegistrationFragment2

    @ContributesAndroidInjector(modules = [])
    abstract fun bindImgEditorFragment() : ImageEditorFragment

    @ContributesAndroidInjector
    abstract fun bindMainFragment() : MainFragment

    @ContributesAndroidInjector
    abstract fun bindSearchFragment() : SearchFragment

    @ContributesAndroidInjector
    abstract fun bindCalendarFragment() : FeedFragment

    @ContributesAndroidInjector
    abstract fun bindChatFragment() : ChatFragment

    @ContributesAndroidInjector
    abstract fun bindNotificationFragment() : NotificationFragment

    @ContributesAndroidInjector
    abstract fun bindProfileFragment() : ProfileFragment

    @ContributesAndroidInjector
    abstract fun bindSearchResultFragment(): SearchResultFragment

    @ContributesAndroidInjector
    abstract fun bindServiceCardFragment(): ServiceCardFragment


}