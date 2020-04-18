package com.akv.akvandroidapprelease.di.auth

import androidx.lifecycle.ViewModel
import com.akv.akvandroidapprelease.di.ViewModelKey
import com.akv.akvandroidapprelease.ui.auth.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel): ViewModel

}