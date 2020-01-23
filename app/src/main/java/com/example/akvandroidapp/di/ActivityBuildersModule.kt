package com.example.akvandroidapp.di


import com.example.akvandroidapp.di.auth.AuthFragmentBuildersModule
import com.example.akvandroidapp.di.auth.AuthModule
import com.example.akvandroidapp.di.auth.AuthScope
import com.example.akvandroidapp.di.auth.AuthViewModelModule
import com.example.akvandroidapp.di.main.MainFragmentBuildersModule
import com.example.akvandroidapp.di.main.MainModule
import com.example.akvandroidapp.di.main.MainScope
import com.example.akvandroidapp.di.main.MainViewModelModule
import com.example.akvandroidapp.di.support.SupportFragmentBuildersModule
import com.example.akvandroidapp.di.support.SupportModule
import com.example.akvandroidapp.di.support.SupportScope
import com.example.akvandroidapp.di.support.SupportViewModelModule
import com.example.akvandroidapp.ui.auth.AuthActivity
import com.example.akvandroidapp.ui.main.MainActivity
import com.example.akvandroidapp.ui.main.messages.MessagesDetailActivity
import com.example.akvandroidapp.ui.main.profile.support.SupportProfileActivity
import com.example.akvandroidapp.ui.main.search.MapActivity
import com.example.akvandroidapp.ui.main.search.zhilye.ZhilyeActivity
import com.example.akvandroidapp.ui.main.search.zhilye.ZhilyeBookActivity
import com.example.akvandroidapp.ui.main.search.zhilye.ZhilyeReviewActivity
import com.example.akvandroidapp.ui.main.search.zhilye.ZhilyeRulesOfHouseActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [AuthModule::class, AuthFragmentBuildersModule::class, AuthViewModelModule::class]
    )
    abstract fun contributeAuthActivity(): AuthActivity

    @MainScope
    @ContributesAndroidInjector(
        modules = [MainModule::class, MainFragmentBuildersModule::class, MainViewModelModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity

    @MainScope
    @ContributesAndroidInjector
    abstract fun contributeMapActivity(): MapActivity

    @MainScope
    @ContributesAndroidInjector(modules = [MainModule::class,MainViewModelModule::class])
    abstract fun contributeZhilyeFragmentActivity(): ZhilyeActivity

    @MainScope
    @ContributesAndroidInjector(modules = [MainModule::class,MainViewModelModule::class])
    abstract fun contributeZhilyeBookActivity(): ZhilyeBookActivity

    @MainScope
    @ContributesAndroidInjector(modules = [MainModule::class,MainViewModelModule::class])
    abstract fun contributeZhilyeReviewActivity(): ZhilyeReviewActivity

    @MainScope
    @ContributesAndroidInjector
    abstract fun contributeZhilyeRulesOfHouseActivity(): ZhilyeRulesOfHouseActivity


    @MainScope
    @ContributesAndroidInjector(modules = [MainModule::class,MainViewModelModule::class])
    abstract fun contributeMessagesDetailActivity(): MessagesDetailActivity

    @SupportScope
    @ContributesAndroidInjector(
        modules = [SupportModule::class, SupportFragmentBuildersModule::class, SupportViewModelModule::class]
    )
    abstract fun contributeSupportProfileActivity(): SupportProfileActivity

}