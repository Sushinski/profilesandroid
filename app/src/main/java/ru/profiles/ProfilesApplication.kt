package ru.profiles

import com.facebook.drawee.backends.pipeline.Fresco
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import ru.profiles.di.DaggerAppComponent


class ProfilesApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        Fresco.initialize(this)
        val appComponent = DaggerAppComponent.builder().application(this).appContext(applicationContext).build()
        appComponent.inject(this)
        return appComponent
    }
}