package model

import com.isel.GomokuRoyale.lobby.domain.Challenge
import com.isel.GomokuRoyale.lobby.domain.PlayerInfo
import com.isel.GomokuRoyale.lobby.domain.firstToMove
import model.Player


/**
 * Represents a Tic-Tac-Toe game. Instances are immutable.
 * @property localPlayerMarker  The local player marker
 * @property forfeitedBy        The marker of the player who forfeited the game, if that was the case
 * @property board              The game board
 */
data class Game(
    val localPlayer: Player = Player.firstToMove,
    val forfeitedBy: Player? = null,
    val board: Board = Board()
)

/**
 * Makes a move on this [Game], returning a new instance.
 * @param at the coordinates where the move is to be made
 * @return the new [Game] instance
 * @throws IllegalStateException if its an invalid move, either because its
 * not the local player's turn or the move cannot be made on that location
 */
fun Game.makeMove(at: Coordinate): Game {
    check(localPlayer == board.turn)
    return copy(board = board.makeMove(at), localPlayer = localPlayer.other())
}

/**
 * Gets which marker is to be assigned to the local player for the given challenge.
 */
fun getLocalPlayerMarker(localPlayer: PlayerInfo) = Player.BLACK


/**
 * Gets the game current result
 */
fun Game.getResult() =
    if (forfeitedBy != null) HasWinner(forfeitedBy.other())
    else board.getResult()

data class Lounge(val uuid: String, val player1: String, val variant: Int)