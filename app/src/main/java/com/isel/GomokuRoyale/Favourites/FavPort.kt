package com.isel.GomokuRoyale.Favourites

import kotlinx.coroutines.flow.Flow
import model.Board
import model.Game

/**
 * Sum type used to describe events occurring while the player is in the lobby.
 *
 * [RosterUpdated] to describe changes in the set of players in the lobby
 * [ChallengeReceived] when a challenge is received by the local player.
 */
sealed class FavEvent
data class RosterUpdated(val favourites: List<GameInfo>) : FavEvent()


/**
 * Abstraction that characterizes the game's lobby.
 */
interface Fav {
    /**
     * Gets the list of players currently in the lobby
     * @return the list of players currently in the lobby
     */
    suspend fun getFavourites(): List<GameInfo>

    /**
     * Enters the lobby. It cannot be entered again until left.
     * @return the list of players currently in the lobby
     * @throws IllegalStateException    if the lobby was already entered
     */
    suspend fun enter(favGame: GameInfo): List<GameInfo>

    fun enterAndObserve(): Flow<FavEvent>

   suspend fun leave()
    suspend fun select(info: GameInfo): Game


}