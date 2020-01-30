package com.example.akvandroidapp.di.myHouse

import androidx.lifecycle.ViewModel
import com.example.akvandroidapp.di.ViewModelKey
import com.example.akvandroidapp.ui.auth.AuthViewModel
import com.example.akvandroidapp.ui.main.profile.add_ad.AddAdViewModel
import com.example.akvandroidapp.ui.main.profile.my_house.MyHouseViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MyHouseViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MyHouseViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: MyHouseViewModel): ViewModel

}