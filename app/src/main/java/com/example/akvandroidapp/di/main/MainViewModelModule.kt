package com.example.akvandroidapp.di.main


import androidx.lifecycle.ViewModel
import com.example.akvandroidapp.di.ViewModelKey
import com.example.akvandroidapp.ui.main.favorite.viewmodel.FavoriteViewModel
import com.example.akvandroidapp.ui.main.home.viewmodel.HomeViewModel
import com.example.akvandroidapp.ui.main.messages.detailState.DetailsViewModel
import com.example.akvandroidapp.ui.main.messages.viewmodel.MessagesViewModel
import com.example.akvandroidapp.ui.main.messages.viewmodel.RequestViewModel
import com.example.akvandroidapp.ui.main.profile.viewmodel.ProfileViewModel
import com.example.akvandroidapp.ui.main.search.viewmodel.SearchViewModel
import com.example.akvandroidapp.ui.main.search.zhilye.viewmodels.ZhilyeBookViewModel
import com.example.akvandroidapp.ui.main.search.zhilye.ZhilyeViewModel
import com.example.akvandroidapp.ui.main.search.zhilye.viewmodels.ZhilyeReviewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    abstract fun bindFavoriteViewModel(favoriteViewModel: FavoriteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(MessagesViewModel::class)
    abstract fun bindMessagesViewModel(messagesViewModel: MessagesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RequestViewModel::class)
    abstract fun bindRequestViewModel(requestViewModel: RequestViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    abstract fun bindMessagesDetailViewModel(detailsViewModel: DetailsViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(ZhilyeBookViewModel::class)
    abstract fun bindZhilyeBookViewModel(zhilyeBookViewModel: ZhilyeBookViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ZhilyeViewModel::class)
    abstract fun bindZhilyeViewModel(zhilyeViewModel: ZhilyeViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(ZhilyeReviewViewModel::class)
    abstract fun bindZhilyeReviewViewModel(zhilyeReviewViewModel: ZhilyeReviewViewModel): ViewModel

}








