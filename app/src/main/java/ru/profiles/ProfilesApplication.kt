package ru.profiles

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import ru.profiles.di.DaggerAppComponent


class ProfilesApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder().application(this).context(applicationContext).build()
        appComponent.inject(this)
        return appComponent
    }
}