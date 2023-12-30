package com.isel.GomokuRoyale.home.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.isel.GomokuRoyale.game.IOState
import com.isel.GomokuRoyale.game.Loaded
import com.isel.GomokuRoyale.game.idle
import com.isel.GomokuRoyale.preferences.model.UserInfo
import com.isel.GomokuRoyale.preferences.model.UserInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeViewModel(userRepo: UserInfoRepository):ViewModel(){

    companion object {
        fun factory(repository: UserInfoRepository) = viewModelFactory {
            initializer { HomeViewModel(repository) }
        }
    }


    private var _isLoggedIn = MutableStateFlow<Boolean>(runBlocking<Boolean>{ userRepo.getUserInfo() != null })
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _userInfoFlow: MutableStateFlow<IOState<UserInfo?>> = MutableStateFlow(idle())


    val userInfo: Flow<IOState<UserInfo?>> get() = _userInfoFlow.asStateFlow()

    /**
     * Resets the view model to the idle state. From the idle state, the user information
     * can be fetched again.
     * @throws IllegalStateException if the view model is not in the loaded state.
     */
    fun resetToIdle() {
        if (_userInfoFlow.value !is Loaded)
            throw IllegalStateException("The view model is not in the loaded state.")
        _userInfoFlow.value = idle()
    }
}