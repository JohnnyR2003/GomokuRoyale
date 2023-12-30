package com.isel.GomokuRoyale.Favourites.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.isel.GomokuRoyale.ui.NavigationHandlers
import com.isel.GomokuRoyale.ui.TopBar
import com.isel.GomokuRoyale.ui.theme.GomokuRoyaleTheme
import model.Board
import model.Coordinate
import model.Game
import model.Player
import model.openingrule
import model.variantes
import ui.BoardView

internal const val FavoriteGameScreenTag = "FavoriteGameScreen"
internal const val FavoriteGameScreenTitleTag = "FavoriteGameScreenTitle"
internal const val FavoriteForfeitButtonTag = "ForfeitButton"
internal const val FavoriteNextButtonTag = "NextButton"
internal const val FavoritePreviousButtonTag = "PreviousButton"
//nomejogo,nomeoponente,data,hora
data class FavoriteGameScreenState(
    @StringRes val title: Int?,
    val game: Game
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteGameScreen(
    onBackRequest: () -> Unit,
    state: FavoriteGameScreenState,
    //onMoveRequested: (Coordinate) -> Unit = { },
    onPreviousRequest: () -> Unit = { },
    onNextRequest: () -> Unit = { },
    //onForfeitRequested: () -> Unit = { },
) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(FavoriteGameScreenTag),
            topBar = { TopBar(navigation = NavigationHandlers(onBackRequest, null)) }
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
                    modifier = Modifier.testTag(FavoriteGameScreenTitleTag)
                )
                BoardView(
                    board = state.game.board,
                    onTileSelected = {  }, ///onMoveRequested
                    enabled = state.game.localPlayer == state.game.board.turn,
                    modifier = Modifier
                        .padding(32.dp)
                        .weight(1.0f, true)
                        .fillMaxSize()
                )
                Row {
                    Button(
                        onClick = onNextRequest,
                        modifier = Modifier.testTag(FavoriteNextButtonTag)
                    ) {
                        Text(text = stringResource(id = R.string.game_screen_next))
                    }
                    Button(
                        onClick = onPreviousRequest,
                        modifier = Modifier.testTag(FavoritePreviousButtonTag)
                    ) {
                        Text(text = stringResource(id = R.string.game_screen_previous))
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteGameScreenPreview() {
    FavoriteGameScreen({},state = FavoriteGameScreenState(
        title = null,
        Game(localPlayer = Player.BLACK, board = aBoard)
    )
    )
}

@Preview(showBackground = true)
@Composable
private fun FavoriteGameScreenWaiting() {

    FavoriteGameScreen({},state = FavoriteGameScreenState(
        title = R.string.game_screen_waiting,
        Game(localPlayer = Player.BLACK, board = Board(turn = Player.BLACK))
    )
    )
}
private val aVariante = variantes.NORMAL
private val aBoardSize = if (aVariante == variantes.OMOK) 19  else 15
private val aBoard = Board(
    turn = Player.BLACK,
    variantes = aVariante,
    openingrule = openingrule.PRO,
    boardSize = aBoardSize,
    moves = mapOf(
        Coordinate(7,7, aBoardSize) to Player.BLACK,
        Coordinate(1,7, aBoardSize) to Player.WHITE,
        Coordinate(5,7, aBoardSize) to Player.BLACK,
        Coordinate(2,7, aBoardSize) to Player.WHITE,
        Coordinate(0,0, aBoardSize) to Player.BLACK,
        Coordinate(14,14, aBoardSize) to Player.WHITE,
        Coordinate(1,0, aBoardSize) to Player.BLACK,
        Coordinate(0,1, aBoardSize) to Player.WHITE,)
)


