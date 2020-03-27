package com.akv.akvandroidapp.di.myHouse

import androidx.lifecycle.ViewModel
import com.akv.akvandroidapp.di.ViewModelKey
import com.akv.akvandroidapp.ui.main.profile.my_house.state.MyHouseViewModel
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