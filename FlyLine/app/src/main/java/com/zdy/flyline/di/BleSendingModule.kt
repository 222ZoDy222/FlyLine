package com.zdy.flyline.di

import com.zdy.flyline.BLE.Repository.bluetoothModels.BleConnectionModel
import com.zdy.flyline.BLE.Repository.bluetoothModels.BleSendingModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BleSendingModule{

    @Provides
    @Singleton
    fun provideBleSendingModel() : BleSendingModel {
        return BleSendingModel()
    }

}