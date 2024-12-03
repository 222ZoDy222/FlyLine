package com.zdy.flyline.di

import com.zdy.flyline.protocol.parameters.MenuParameters
import com.zdy.flyline.utils.settingsMenues.SettingsMenu_main
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
class ProtocolModule {

    @Provides
    @FragmentScoped
    fun provideProtocolMenu() : MenuParameters {
        return SettingsMenu_main.getMenu()
    }

}