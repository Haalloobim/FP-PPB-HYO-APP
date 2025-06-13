package com.app.hyo.di

import android.app.Application
import com.app.hyo.data.manger.LocalUserMangerImpl
import com.app.hyo.data.manger.UserDataStoreRepositoryImpl
import com.app.hyo.domain.manger.LocalUserManger
import com.app.hyo.domain.manger.UserRepository
import com.app.hyo.domain.usecases.app_entry.AppEntryUseCases
import com.app.hyo.domain.usecases.app_entry.ReadAppEntry
import com.app.hyo.domain.usecases.app_entry.SaveAppEntry
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalUserManger(
        application: Application
    ): LocalUserManger = LocalUserMangerImpl(context = application)

    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManger: LocalUserManger
    ): AppEntryUseCases = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManger),
        saveAppEntry = SaveAppEntry(localUserManger)
    )

    @Provides
    @Singleton
    fun provideUserRepository(
        application: Application
    ): UserRepository = UserDataStoreRepositoryImpl(context = application)
}