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
import com.isel.GomokuRoyale.preferences.model.UserInfo
import com.isel.GomokuRoyale.utils.viewModelInit
import model.Board
import model.Game
import model.Player
import model.getResult
import model.openingrule
import model.toOpeningRule
import model.toVariante
import model.variantes
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
        fun navigate(origin: Context, localPlayer: Player, variante: String , openingRule: String) {
            with(origin) {
                startActivity(
                    Intent(this, GameActivity::class.java).also {
                        it.putExtra(MATCH_INFO_EXTRA, MatchInfo(localPlayer.toString(), localPlayer.other().toString(),variante,openingRule))
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

                else -> {}
            }
        }

        if (viewModel.state == MatchState.IDLE)
            viewModel.startMatch(localPlayer = Player.BLACK, board = board)

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


    private val board : Board by lazy {
        Board(variantes = matchInfo.variant.toVariante(), openingrule = matchInfo.openingRule.toOpeningRule())
    }

    @Parcelize
    internal data class MatchInfo(
        val localPlayerId: String,
        val opponentId: String,
        val variant: String,
        val openingRule: String

    ) : Parcelable

    private fun MatchInfo(localPlayer: Player, variante: String, openingRule: String): MatchInfo {
        val opponent = localPlayer.other()

        return MatchInfo(
            localPlayerId = localPlayer.toString(),
            opponentId = opponent.toString(),
            variant = variante,
            openingRule = openingRule
        )
    }
}








