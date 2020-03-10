package com.akv.akvandroidapp.ui.main.favorite.state


sealed class FavoriteStateEvent {

    object FavoriteMyListEvent: FavoriteStateEvent()

    object DeleteFavoriteItemEvent: FavoriteStateEvent()

    object None: FavoriteStateEvent()
}