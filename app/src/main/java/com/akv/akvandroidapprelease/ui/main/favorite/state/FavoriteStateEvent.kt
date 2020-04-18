package com.akv.akvandroidapprelease.ui.main.favorite.state


sealed class FavoriteStateEvent {

    object FavoriteMyListEvent: FavoriteStateEvent()

    object DeleteFavoriteItemEvent: FavoriteStateEvent()

    object None: FavoriteStateEvent()
}