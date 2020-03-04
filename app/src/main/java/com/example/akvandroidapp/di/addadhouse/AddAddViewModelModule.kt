package com.example.akvandroidapp.di.addadhouse

import androidx.lifecycle.ViewModel
import com.example.akvandroidapp.di.ViewModelKey
import com.example.akvandroidapp.ui.main.profile.add_ad.AddAdViewModel
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