package com.isel.GomokuRoyale.Favourites.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.parcelize.Parcelize
import com.isel.GomokuRoyale.game.adapters.toMovesList
import model.Board
import model.Coordinate
import model.Game
import model.Player
import model.moves
import model.toOpeningRule
import model.toPlayer
import model.toVariante
import java.util.*





class FavouritesActivity: ComponentActivity() {

    /*private val viewModel by viewModels<GameScreenViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer).match
            GameScreenViewModel(app)
        }
    }*/

    companion object {
        const val MATCH_INFO_EXTRA = "MATCH_INFO_EXTRA"
        fun navigateTo(origin: Context,aGame: Game) {
            with(origin) {
                startActivity(
                    Intent(this, FavouritesActivity::class.java).also {
                        it.putExtra(
                            MATCH_INFO_EXTRA,
                            MatchInfo(
                                aGame.board.title,
                                aGame.board.turn.toString(),
                                aGame.board.variantes.toString(),
                                aGame.board.openingrule.toString(),
                                aGame.board.boardSize,
                                aGame.board.toMovesList().joinToString(","),
                                aGame.forfeitedBy))
                    }
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val currentMove = remember {
                mutableStateOf(0)
            }

            val currentGame = remember {
                mutableStateOf(Game(board = Board(variantes= board.variantes, openingrule = board.openingrule)))
            }
            //val currentState = viewModel.state
            val title = null
            /*when (currentState) {
            MatchState.STARTING -> R.string.game_screen_waiting
            MatchState.IDLE -> R.string.game_screen_waiting
            else -> null
        }*/

            FavoriteGameScreen(
                onBackRequest = { finish() },
                state = FavoriteGameScreenState(title, currentGame.value),
                //onMoveRequested = { at -> viewModel.makeMove(at) },
                onNextRequest = {
                    if (currentMove.value< board.moves.size) {
                        //viewModel.end()
                        currentMove.value++
                        //viewModel.state = MatchState.IDLE
                        val board1 = getCurrentBoard(currentMove.value)
                        currentGame.value = Game(board1.turn,if(board1.moves.size==board.moves.size) matchInfo.forfeitedBy else null,board1)
                        //viewModel.startMatch(localPlayer = board.turn, board = board)
                    }
                },
                onPreviousRequest = {
                    if (currentMove.value>0) {
                        //viewModel.end()
                        currentMove.value--
                        val board1 = getCurrentBoard(currentMove.value)
                        currentGame.value = Game(board1.turn,if(board1.moves.size==board.moves.size) matchInfo.forfeitedBy else null,board1)
                        //viewModel.startMatch(localPlayer = board.turn, board = board)
                    }
                },
                //onForfeitRequested = { viewModel.forfeit() }
            )

            /*when (currentState) {
                MatchState.STARTING -> StartingMatchDialog()
                MatchState.FINISHED -> MatchEndedDialog(
                    localPLayerMarker = currentGame.localPlayer,
                    result = currentGame.getResult(),
                    onDismissRequested = { finish() }
                )

                else -> {}
            }*/
        }

        /*if (viewModel.state == MatchState.IDLE)
            viewModel.startMatch(localPlayer = Player.BLACK, board = board)
        */
        /*onBackPressedDispatcher.addCallback(owner = this, enabled = true) {
            viewModel.forfeit()
            finish()
        }*/
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
        Board.fromMovesList(title = matchInfo.title, turn = matchInfo.turn.toPlayer(), variantes = matchInfo.variant.toVariante(), openingrule = matchInfo.openingRule.toOpeningRule(), moves = matchInfo.moves.toMovesList())
    }

    @Parcelize
    internal data class MatchInfo(
        val title: String,
        val turn: String,
        val variant: String,
        val openingRule: String,
        val boardSize:Int,
        val moves:String,
        val forfeitedBy: Player?,


        ) : Parcelable

    private fun MatchInfo(aGame: Game): MatchInfo {
        val opponent = aGame.localPlayer.other()

        return MatchInfo(
            title = aGame.board.title,
            turn = aGame.board.turn.toString(),
            variant = aGame.board.variantes.toString(),
            openingRule = aGame.board.openingrule.toString(),
            boardSize = aGame.board.boardSize,
            moves = aGame.board.moves.map { it.key.toString() }.joinToString(","),
            forfeitedBy = aGame.forfeitedBy
        )
    }

    private fun getCurrentBoard(currentMove:Int):Board{
        if (currentMove ==0)return Board(variantes = board.variantes, openingrule = board.openingrule, boardSize = board.boardSize)
        var y = 1
        val moves = mutableMapOf<Coordinate,Player>()
        for(i in board.moves){
            moves.put(i.key,i.value)
            if (y == currentMove)break
            y++
        }
        val turn = if (y %2 == 0)Player.BLACK else Player.WHITE
        return Board(turn = turn, variantes = board.variantes, openingrule = board.openingrule, boardSize = board.boardSize, moves = moves.toMap())
    }
    private fun getCurrentMove(currentMove: Int): Map.Entry<Coordinate, Player>? {
        var y = 1
        for(i in board.moves){
            if (y == currentMove){return i}
            y++
        }
        return null
    }
}