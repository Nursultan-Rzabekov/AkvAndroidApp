package com.akv.akvandroidapp.di.auth

import com.akv.akvandroidapp.ui.auth.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeLauncherFragment(): LauncherFragment

    @ContributesAndroidInjector()
    abstract fun contributeRegisterUpFragment(): RegisterUpFragment

    @ContributesAndroidInjector()
    abstract fun contributeRegisterPassFragment(): RegisterPassFragment

    @ContributesAndroidInjector()
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector()
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector()
    abstract fun contributeForgotPasswordFragment(): ForgotPasswordFragment

    @ContributesAndroidInjector()
    abstract fun contributeTermOfUseFragment(): TermOfUseFragment

    @ContributesAndroidInjector()
    abstract fun contributePrivacyPolicyFragment(): PrivacyPolicyFragment

    @ContributesAndroidInjector()
    abstract fun contributeResetCodeFragment(): ResetCodeFragment

    @ContributesAndroidInjector()
    abstract fun contributeLoginGmailFragment(): LoginGmailFragment

    @ContributesAndroidInjector()
    abstract fun contributeNewPasswordFragment(): NewPasswordFragment

}