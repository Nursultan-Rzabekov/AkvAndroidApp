package com.akv.akvandroidapp.di.addadhouse

import com.akv.akvandroidapp.ui.main.profile.add_ad.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AddAddFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeProfileAddAddressFragment(): ProfileAddAddressFragment

    @ContributesAndroidInjector()
    abstract fun contributeProfileProfileAddCheckFragment(): ProfileAddCheckFragment

    @ContributesAndroidInjector()
    abstract fun contributeProfileProfileAddDescriptionFragment(): ProfileAddDescriptionFragment

    @ContributesAndroidInjector()
    abstract fun contributeProfileProfileAddGalleryFragment(): ProfileAddGalleryFragment

    @ContributesAndroidInjector()
    abstract fun contributeProfileAddNearFragment(): ProfileAddNearFragment

    @ContributesAndroidInjector()
    abstract fun contributeProfileAddDatesFragment(): ProfileAddDatesFragment

    @ContributesAndroidInjector()
    abstract fun contributePProfileAddPriceFragment(): ProfileAddPriceFragment

    @ContributesAndroidInjector()
    abstract fun contributeProfileAddQuestsFragment(): ProfileAddQuestsFragment

    @ContributesAndroidInjector()
    abstract fun contributeProfileAddRequirementFragment(): ProfileAddRequirementFragment

    @ContributesAndroidInjector()
    abstract fun contributeProfileAddRulesFragment(): ProfileAddRulesFragment

    @ContributesAndroidInjector()
    abstract fun contributeProfileAddTypeFragment(): ProfileAddTypeFragment

    @ContributesAndroidInjector()
    abstract fun contributeProfileAddPostFragment(): ProfileAddPostFragment

}