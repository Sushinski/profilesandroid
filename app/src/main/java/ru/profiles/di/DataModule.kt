package ru.profiles.di

import dagger.Module
import dagger.Provides
import ru.profiles.ProfilesApplication
import ru.profiles.api.interfaces.AuthApi
import ru.profiles.api.interfaces.RegistrationApi
import ru.profiles.dao.AuthModelDao
import ru.profiles.dao.UserModelDao
import ru.profiles.data.AppDatabase
import ru.profiles.data.AuthRepository
import ru.profiles.data.RegistrationRepository
import ru.profiles.data.UserRepository
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
}