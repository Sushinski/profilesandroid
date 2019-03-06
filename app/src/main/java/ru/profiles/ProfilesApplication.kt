package ru.profiles

import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import ru.profiles.di.DaggerAppComponent


class ProfilesApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(applicationContext)
        Stetho.initialize(Stetho.newInitializerBuilder(this)
            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
            .build())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder().application(this).appContext(applicationContext).build()
        appComponent.inject(this)
        return appComponent
    }
}