package com.example.akvandroidapp.di.auth

import com.example.akvandroidapp.ui.auth.ForgotPasswordFragment
import com.example.akvandroidapp.ui.auth.LauncherFragment
import com.example.akvandroidapp.ui.auth.LoginFragment
import com.example.akvandroidapp.ui.auth.RegisterFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeLauncherFragment(): LauncherFragment

    @ContributesAndroidInjector()
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector()
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector()
    abstract fun contributeForgotPasswordFragment(): ForgotPasswordFragment

}