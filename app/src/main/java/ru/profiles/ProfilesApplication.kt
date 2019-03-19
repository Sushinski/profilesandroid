package ru.profiles

import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.util.ByteConstants
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import ru.profiles.di.DaggerAppComponent


class ProfilesApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        val diskCacheConfig = DiskCacheConfig.newBuilder(this).setBaseDirectoryPath(this.cacheDir)
            .setBaseDirectoryName("image")
            .setMaxCacheSize((100 * ByteConstants.MB).toLong())
            .setMaxCacheSizeOnLowDiskSpace((10 * ByteConstants.MB).toLong())
            .setMaxCacheSizeOnVeryLowDiskSpace((5 * ByteConstants.MB).toLong())
            .setVersion(1)
            .build()

        val imagePipelineConfig = ImagePipelineConfig.newBuilder(this)
            .setMainDiskCacheConfig(diskCacheConfig)
            .build()
        Fresco.initialize(applicationContext, imagePipelineConfig)
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