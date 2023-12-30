package model


import model.Player


/**
 * Represents a GomokuRoyale game. Instances are immutable.
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
 */
fun Game.makeMove(at: Coordinate): Game {
    check(localPlayer == board.turn)
    return copy(board = board.makeMove(at), localPlayer = localPlayer.other())
}

/**
 * Gets which marker is to be assigned to the local player for the given match.
 */
fun getLocalPlayerMarker(localPlayer: Player) = Player.BLACK


/**
 * Gets the game current result
 */
fun Game.getResult() =
    if (forfeitedBy != null) HasWinner(forfeitedBy.other())
    else board.getResult()

