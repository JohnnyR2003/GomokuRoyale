package ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import model.Coordinate
import model.Game
import model.GameEnded
import model.GameStarted
import model.Match
import model.OnGoing
import model.Player
import model.getResult

/**
 * Represents the current match state
 */
enum class MatchState { IDLE, STARTING, STARTED, FINISHED }

/**
 * View model for the Game Screen hosted by [GameActivity].
 */
class GameScreenViewModel(private val match: Match) : ViewModel() {

    private val _onGoingGame = MutableStateFlow(Game())
    val onGoingGame = _onGoingGame.asStateFlow()

    private var _state by mutableStateOf(MatchState.IDLE)
    val state: MatchState
        get() = _state

    fun startMatch(localPlayer: Player): Job? =
        if (state == MatchState.IDLE) {
            _state = MatchState.STARTING
            viewModelScope.launch {
                match.start(localPlayer).collect {
                    _onGoingGame.value = it.game
                    _state = when (it) {
                        is GameStarted -> MatchState.STARTED
                        is GameEnded -> MatchState.FINISHED
                        else ->
                            if (it.game.getResult() !is OnGoing) MatchState.FINISHED

                            else MatchState.STARTED
                    }

                    if (_state == MatchState.FINISHED)
                        match.end()
                }
            }
        }
        else null

    fun makeMove(at: Coordinate): Job? =
        if (state == MatchState.STARTED) {
            viewModelScope.launch {
                match.makeMove(at)
            }
        }
        else null

    fun forfeit(): Job? =
        if (state == MatchState.STARTED) viewModelScope.launch { match.forfeit() }
        else null
}

