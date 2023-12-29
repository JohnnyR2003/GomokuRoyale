package com.isel.GomokuRoyale.preferences.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.isel.GomokuRoyale.game.IOState
import com.isel.GomokuRoyale.game.Saving
import com.isel.GomokuRoyale.game.idle
import com.isel.GomokuRoyale.game.saved
import com.isel.GomokuRoyale.game.saving
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserPreferencesScreenViewModel(
    private val repository: UserInfoRepository
) : ViewModel() {

    companion object {
        fun factory(repository: UserInfoRepository) = viewModelFactory {
            initializer { UserPreferencesScreenViewModel(repository) }
        }
    }

    private val _ioStateFlow: MutableStateFlow<IOState<UserInfo?>> = MutableStateFlow(idle())

    /**
     * The flow of states the view model traverses.
     */
    val ioState: Flow<IOState<UserInfo?>>
        get() = _ioStateFlow.asStateFlow()

    /**
     * Updates the user information.
     * @param userInfo  The user information.
     * @throws IllegalStateException if the view model is still in the saving state because the
     * user information is still being updated.
     */
    fun updateUserInfo(userInfo: UserInfo) {
        if (_ioStateFlow.value is Saving)
            throw IllegalStateException("The view model is not in the idle state.")

        _ioStateFlow.value = saving()
        viewModelScope.launch {
            val result = runCatching {
                repository.updateUserInfo(userInfo)
                userInfo
            }
            _ioStateFlow.value = saved(result)
        }
    }

     suspend fun getUserInfo(): UserInfo? {
        return repository.getUserInfo()
    }

    /**
     * Resets the view model to the idle state. From the idle state, the user information
     * can be updated.
     * @throws IllegalStateException if the view model is not in the saving state.
     */
    fun resetToIdle() {
        if (_ioStateFlow.value !is Saving)
            throw IllegalStateException("The view model is not in the saving state.")
        _ioStateFlow.value = idle()
    }
}