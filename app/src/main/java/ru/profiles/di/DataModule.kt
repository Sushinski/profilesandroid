package ru.profiles.di

import dagger.Module
import dagger.Provides
import ru.profiles.ProfilesApplication
import ru.profiles.api.interfaces.AuthApi
import ru.profiles.api.interfaces.RegistrationApi
import ru.profiles.api.interfaces.ResourcesApi
import ru.profiles.api.interfaces.ServicesApi
import ru.profiles.dao.AuthModelDao
import ru.profiles.dao.GoodsModelDao
import ru.profiles.dao.ServicesModelDao
import ru.profiles.dao.UserModelDao
import ru.profiles.data.*
import javax.inject.Singleton

@Module(includes = [ApiModule::class])
class DataModule {

    @Singleton
    @Provides
    fun provideDb(app: ProfilesApplication): AppDatabase {
        return AppDatabase.getInstance(app)
    }

    @Singleton
    @Provides
    fun provideRepoDao(db: AppDatabase): AuthModelDao {
        return db.authModelDao()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase): UserModelDao {
        return db.userModelDao()
    }

    @Singleton
    @Provides
    fun provideServicesDao(db: AppDatabase): ServicesModelDao{
        return db.servicesModelDao()
    }

    @Singleton
    @Provides
    fun provideGoodsDao(db: AppDatabase): GoodsModelDao {
        return db.goodsModelDao()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authDao: AuthModelDao, authApi: AuthApi): AuthRepository {
        return AuthRepository.getInstance(authDao, authApi)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserModelDao, authApi: AuthApi): UserRepository{
        return UserRepository.getInstance(userDao, authApi)
    }

    @Provides
    @Singleton
    fun provideRegistrationRepository(registrationApi: RegistrationApi): RegistrationRepository{
        return RegistrationRepository.getInstance(registrationApi)
    }

    @Provides
    @Singleton
    fun providesResourcesRepo(authDao: AuthModelDao, resources: ResourcesApi): ResourcesRepository{
        return ResourcesRepository.getInstance(authDao, resources)
    }

    @Provides
    @Singleton
    fun providesServicesRepository(searchApi: ServicesApi, servicesDao: ServicesModelDao): ServicesRepository{
        return ServicesRepository.getInstance(searchApi, servicesDao)
    }

    @Provides
    @Singleton
    fun providesGoodsRepository(goodsDao: GoodsModelDao): GoodsRepository{
        return GoodsRepository.getInstance(goodsDao)
    }

}