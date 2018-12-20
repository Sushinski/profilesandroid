package ru.profiles.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import ru.profiles.ProfilesApplication
import javax.inject.Singleton


@Component(
    modules = [
        DataModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityBuilderModule::class,
        LoginFragmentModule::class
    ]
)
@Singleton
interface AppComponent: AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: ProfilesApplication): Builder

        @BindsInstance
        fun appContext(context: Context): Builder

        fun build(): AppComponent
    }

    fun inject(app: ProfilesApplication)
}