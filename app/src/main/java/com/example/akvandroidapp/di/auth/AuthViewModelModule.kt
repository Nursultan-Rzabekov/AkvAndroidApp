package com.example.akvandroidapp.di.auth

import androidx.lifecycle.ViewModel
import com.example.akvandroidapp.di.ViewModelKey
import com.example.akvandroidapp.ui.auth.AuthViewModel
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