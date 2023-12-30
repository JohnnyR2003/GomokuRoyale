package com.isel.GomokuRoyale.Favourites.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isel.GomokuRoyale.Favourites.Fav
import com.isel.GomokuRoyale.Favourites.GameInfo
import com.isel.GomokuRoyale.Favourites.RosterUpdated
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavouritesViewModel(val list: Fav): ViewModel() {

    private val _favourites: MutableStateFlow<List<GameInfo>> = MutableStateFlow(emptyList())
    val favourites = _favourites.asStateFlow()

    private val error : MutableStateFlow<String?> = MutableStateFlow(null)
    val rError = error.asStateFlow()


    private var lobbyMonitor: Job? = null

    fun enterLobby(): Job? =
        if (lobbyMonitor == null) {
            val eventObserver = viewModelScope.launch {
                list.enterAndObserve().collect { event ->
                    when(event) {
                        is RosterUpdated -> {
                            _favourites.value = event.favourites
                        }
                    }
                }
            }
            lobbyMonitor = eventObserver
            eventObserver
        } else null

    suspend fun fetchFavourites() {
        _favourites.value = list.getFavourites()
    }

    suspend fun leaveFavourites(){
        list.leave()
    }
    fun resetError() {
        error.value = null
    }



}