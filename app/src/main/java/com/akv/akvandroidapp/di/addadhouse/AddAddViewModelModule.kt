package com.akv.akvandroidapp.di.addadhouse

import androidx.lifecycle.ViewModel
import com.akv.akvandroidapp.di.ViewModelKey
import com.akv.akvandroidapp.ui.main.profile.add_ad.AddAdViewModel
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