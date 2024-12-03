package com.zdy.flyline.di

import com.zdy.flyline.BLE.Repository.bluetoothModels.BleConnectionModel
import com.zdy.flyline.BLE.Repository.bluetoothModels.BluetoothModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BluetoothModule {

    @Provides
    @Singleton
    fun provideBluetoothModel(bluetoothConnectionModel: BleConnectionModel) : BluetoothModel {
        return bluetoothConnectionModel
    }
}