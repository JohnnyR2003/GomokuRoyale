package com.isel.GomokuRoyale.game.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.parcelize.Parcelize
import com.isel.GomokuRoyale.DependenciesContainer
import com.isel.GomokuRoyale.R
import com.isel.GomokuRoyale.lobby.domain.Challenge
import com.isel.GomokuRoyale.lobby.domain.PlayerInfo
import com.isel.GomokuRoyale.preferences.model.UserInfo
import com.isel.GomokuRoyale.utils.viewModelInit
import model.getResult
import ui.GameScreenViewModel
import ui.MatchEndedDialog
import ui.MatchState
import ui.StartingMatchDialog
import java.util.*



class GameActivity: ComponentActivity() {

    private val viewModel by viewModels<GameScreenViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer).match
            GameScreenViewModel(app)
        }
    }

    companion object {
        const val MATCH_INFO_EXTRA = "MATCH_INFO_EXTRA"
        fun navigate(origin: Context, localPlayer: PlayerInfo, challenge: Challenge) {
            with(origin) {
                startActivity(
                    Intent(this, GameActivity::class.java).also {
                        it.putExtra(MATCH_INFO_EXTRA, MatchInfo(localPlayer, challenge))
                    }
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val currentGame by viewModel.onGoingGame.collectAsState()
            val currentState = viewModel.state
            val title = when (currentState) {
                MatchState.STARTING -> R.string.game_screen_waiting
                MatchState.IDLE -> R.string.game_screen_waiting
                else -> null
            }

            GameScreen(
                state = GameScreenState(title, currentGame),
                onMoveRequested = { at -> viewModel.makeMove(at) },
                onForfeitRequested = { viewModel.forfeit() }
            )

            when (currentState) {
                MatchState.STARTING -> StartingMatchDialog()
                MatchState.FINISHED -> MatchEndedDialog(
                    localPLayerMarker = currentGame.localPlayer,
                    result = currentGame.getResult(),
                    onDismissRequested = { finish() }
                )
                else -> { }
            }
        }

        if (viewModel.state == MatchState.IDLE)
            viewModel.startMatch(localPlayer, challenge)

        onBackPressedDispatcher.addCallback(owner = this, enabled = true) {
            viewModel.forfeit()
            finish()
        }
    }

    @Suppress("deprecation")
    private val matchInfo: MatchInfo by lazy {
        val info =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getParcelableExtra(MATCH_INFO_EXTRA, MatchInfo::class.java)
            else
                intent.getParcelableExtra(MATCH_INFO_EXTRA)

        checkNotNull(info)
    }

    private val localPlayer: PlayerInfo by lazy {
        PlayerInfo(
            info = UserInfo(matchInfo.localPlayerNick, " "),  /**ver bearer está diferente**/
            id = UUID.fromString(matchInfo.localPlayerId)
        )
    }

    private val challenge: Challenge by lazy {
        val opponent = PlayerInfo(
            info = UserInfo(matchInfo.opponentNick," "),    /**ver bearer está diferente**/
            id = UUID.fromString(matchInfo.opponentId)
        )

        if (localPlayer.id.toString() == matchInfo.challengerId)
            Challenge(challenger = localPlayer, challenged = opponent)
        else
            Challenge(challenger = opponent, challenged = localPlayer)
    }
}

@Parcelize
internal data class MatchInfo(
    val localPlayerId: String,
    val localPlayerNick: String,
    val opponentId: String,
    val opponentNick: String,
    val challengerId: String,
) : Parcelable

internal fun MatchInfo(localPlayer: PlayerInfo, challenge: Challenge): MatchInfo {
    val opponent =
        if (localPlayer == challenge.challenged) challenge.challenger
        else challenge.challenged

    return MatchInfo(
        localPlayerId = localPlayer.id.toString(),
        localPlayerNick = localPlayer.info.username,
        opponentId = opponent.id.toString(),
        opponentNick = opponent.info.username,
        challengerId = challenge.challenger.id.toString(),
    )
}








