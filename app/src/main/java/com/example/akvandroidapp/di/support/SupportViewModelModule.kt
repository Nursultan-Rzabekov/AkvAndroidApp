package com.example.akvandroidapp.di.support


import androidx.lifecycle.ViewModel
import com.example.akvandroidapp.di.ViewModelKey
import com.example.akvandroidapp.ui.main.profile.support.viewmodel.SupportViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class SupportViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SupportViewModel::class)
    abstract fun bindProfileViewModel(createBlogViewModel: SupportViewModel): ViewModel

}








