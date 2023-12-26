package com.isel.GomokuRoyale.preferences.model

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.isel.GomokuRoyale.DependenciesContainer
import com.isel.GomokuRoyale.TAG
import com.isel.GomokuRoyale.lobby.ui.LobbyActivity
import com.isel.GomokuRoyale.preferences.ui.PreferencesScreen
import com.isel.GomokuRoyale.utils.viewModelInit
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

const val FINISH_ON_SAVE_EXTRA = "FinishOnSaveExtra"

/**
 * The screen used to display and edit the user information to be used to identify
 * the player in the lobby.
 */


class PreferencesActivity : ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer).userInfoRepo
    }

    companion object {
        fun navigateTo(context: Context, finishOnSave: Boolean = false) {
            with(context) {
                val intent = Intent(this, PreferencesActivity::class.java)
                if (finishOnSave) {
                    intent.putExtra(FINISH_ON_SAVE_EXTRA, true)
                }
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PreferencesScreen(
                userInfo = repo.userInfo,
                onBackRequested = { finish() },
                onSaveRequested = {
                    repo.userInfo = it
                    if (intent.getBooleanExtra(FINISH_ON_SAVE_EXTRA, false)) {
                        finish()
                    }
                    else {
                        LobbyActivity.navigateTo(this)
                    }
                }
            )
        }
    }
}