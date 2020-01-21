package com.example.akvandroidapp.ui.main.favorite.state


sealed class FavoriteStateEvent {

    class FavoriteMyListEvent: FavoriteStateEvent()

    class DeleteFavoriteItemEvent: FavoriteStateEvent()

    class None: FavoriteStateEvent()
}