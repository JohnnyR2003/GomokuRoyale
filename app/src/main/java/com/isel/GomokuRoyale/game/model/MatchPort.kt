package model


import com.isel.GomokuRoyale.Favourites.GameInfo
import kotlinx.coroutines.flow.Flow
import model.Player
import java.util.UUID

/**
 * Sum type used to describe events occurring while the match is ongoing.
 *
 * [GameStarted] to signal that the game has started.
 * [MoveMade] to signal that the a move was made.
 * [GameEnded] to signal the game termination.
 */
sealed class GameEvent(val game: Game)
class GameStarted(game: Game) : GameEvent(game)
class MoveMade(game: Game) : GameEvent(game)
class GameEnded(game: Game, val winner: Player? = null) : GameEvent(game)

/**
 * Abstraction that characterizes a match between two players, that is, the
 * required interactions.
 */
interface Match {

    /**
     * @param [localPlayer] the local player information
     * @param [gameId] the game identifier
     * @return the flow of game state change events, expressed as [GameEvent] instances
     * @throws IllegalStateException if a game is in progress
     */

    //fun getFavourites() :List<GameInfo>
    fun start(localPlayer: Player, gameIds: UUID, board: Board): Flow<GameEvent>

    /**
     * Makes a move at the given coordinates.
     */
    suspend fun makeMove(at: Coordinate)

    /**
     * Forfeits the current game.
     * @throws IllegalStateException if a game is not in progress
     */
    suspend fun forfeit()

    /**
     * Ends the match, cleaning up if necessary.
     */
    suspend fun end()
}

