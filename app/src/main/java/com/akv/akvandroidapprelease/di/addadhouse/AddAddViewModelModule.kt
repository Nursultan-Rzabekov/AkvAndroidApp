package com.akv.akvandroidapprelease.di.addadhouse

import androidx.lifecycle.ViewModel
import com.akv.akvandroidapprelease.di.ViewModelKey
import com.akv.akvandroidapprelease.ui.main.profile.add_ad.AddAdViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddAddViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddAdViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AddAdViewModel): ViewModel

}