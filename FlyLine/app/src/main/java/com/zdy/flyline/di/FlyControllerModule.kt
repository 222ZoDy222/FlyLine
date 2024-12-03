package com.zdy.flyline.di

import com.zdy.flyline.BLE.Repository.bluetoothModels.BleSendingModel
import com.zdy.flyline.models.FlyControllerModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FlyControllerModule {

    @Provides
    @Singleton
    fun provideFlyController(sendingModel: BleSendingModel) : FlyControllerModel{
        return FlyControllerModel(sendingModel)
    }


}