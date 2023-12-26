package com.isel.GomokuRoyale.game.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isel.GomokuRoyale.R
import com.isel.GomokuRoyale.ui.theme.GomokuRoyaleTheme
import model.Board
import model.Coordinate
import model.Game
import model.Player
import model.openingrule
import model.variantes
import ui.BoardView

internal const val GameScreenTag = "GameScreen"
internal const val GameScreenTitleTag = "GameScreenTitle"
internal const val ForfeitButtonTag = "ForfeitButton"

data class GameScreenState(
    @StringRes val title: Int?,
    val game: Game
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    state: GameScreenState,
    onMoveRequested: (Coordinate) -> Unit = { },
    onForfeitRequested: () -> Unit = { },
) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(GameScreenTag),
            /*topBar = {
                TopBar()
            },*/
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                val titleTextId = when {
                    state.title != null -> state.title
                    state.game.localPlayer == state.game.board.turn ->
                        R.string.game_screen_your_turn
                    else -> R.string.game_screen_opponent_turn
                }
                Text(
                    text = stringResource(id = titleTextId),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag(GameScreenTitleTag)
                )
                BoardView(
                    board = state.game.board,
                    onTileSelected = onMoveRequested,
                    enabled = state.game.localPlayer == state.game.board.turn,
                    modifier = Modifier
                        .padding(32.dp)
                        .weight(1.0f, true)
                        .fillMaxSize()
                )
                Button(
                    onClick = onForfeitRequested,
                    modifier = Modifier.testTag(ForfeitButtonTag)
                ) {
                    Text(text = stringResource(id = R.string.game_screen_forfeit))
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GameScreenPreview() {
    GameScreen(state = GameScreenState(
        title = null,
        Game(localPlayer = Player.BLACK, board = aBoard)
    ))
}

@Preview(showBackground = true)
@Composable
private fun GameScreenWaiting() {

    GameScreen(state = GameScreenState(
        title = R.string.game_screen_waiting,
        Game(localPlayer = Player.BLACK, board = Board(Player.BLACK))
    ))
}
private val aVariante = variantes.NORMAL
private val aBoardSize = if (aVariante == variantes.OMOK) 19  else 15
private val aBoard = Board(
    turn = Player.BLACK,
    aVariante,
    openingrule.PRO,
    aBoardSize,
    moves = mapOf(
        Coordinate(7,7,aBoardSize) to Player.BLACK,
        Coordinate(1,7,aBoardSize) to Player.WHITE,
        Coordinate(5,7,aBoardSize) to Player.BLACK,
        Coordinate(2,7,aBoardSize) to Player.WHITE,
        Coordinate(0,0,aBoardSize) to Player.BLACK,
        Coordinate(14,14,aBoardSize) to Player.WHITE,
        Coordinate(1,0,aBoardSize) to Player.BLACK,
        Coordinate(0,1,aBoardSize) to Player.WHITE,)
)


