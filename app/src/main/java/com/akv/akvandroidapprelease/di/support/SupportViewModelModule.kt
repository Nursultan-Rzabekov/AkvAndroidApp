package com.akv.akvandroidapprelease.di.support


import androidx.lifecycle.ViewModel
import com.akv.akvandroidapprelease.di.ViewModelKey
import com.akv.akvandroidapprelease.ui.main.profile.support.viewmodel.SupportViewModel
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








