package com.isel.GomokuRoyale.lobby.ui

import LobbyScreen
import LobbyState
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.isel.GomokuRoyale.DependenciesContainer
import com.isel.GomokuRoyale.game.ui.GameActivity
import com.isel.GomokuRoyale.lobby.model.LobbyViewModel
import com.isel.GomokuRoyale.preferences.model.PreferencesActivity
import com.isel.GomokuRoyale.utils.viewModelInit
import kotlinx.coroutines.flow.filter

import kotlinx.coroutines.launch

class LobbyActivity : ComponentActivity() {

    private val viewModel :LobbyViewModel by viewModels {
        viewModelInit {
            val app = (application as DependenciesContainer)
            LobbyViewModel(app.lobby, app.userInfoRepo)
        }
    }

    companion object {
        fun navigateTo(context: Context) {
            with(context) {
                val intent = Intent(this, LobbyActivity::class.java)
                startActivity(intent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val players by viewModel.players.collectAsState()
            LobbyScreen(
                state = LobbyState(players),
                onPlayerSelected = { player -> viewModel.sendChallenge(player) },
                onBackRequested = { finish() },
                onPreferencesRequested = {
                PreferencesActivity.navigateTo(this,finishOnSave = true)
                }
            )
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.players.filter { it == viewModel.players.value }
                viewModel.enterLobby()
                try {
                    viewModel.pendingMatch.collect {
                        if (it != null) {
                            GameActivity.navigate(
                                origin = this@LobbyActivity,
                                localPlayer = it.localPlayer,
                                challenge = it.challenge,
                                )

                        }
                    }
                }
                finally {
                    viewModel.leaveLobby()
                }
            }
        }
    }
}
