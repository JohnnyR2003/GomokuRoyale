package com.isel.GomokuRoyale.Favourites.adapters

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.isel.GomokuRoyale.Favourites.Fav
import com.isel.GomokuRoyale.Favourites.FavEvent
import com.isel.GomokuRoyale.Favourites.GameInfo
import com.isel.GomokuRoyale.Favourites.RosterUpdated
import com.isel.GomokuRoyale.game.adapters.toMovesList
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import model.Board
import model.Coordinate
import model.Game
import model.Player
import model.moves
import model.openingrule
import model.toOpeningRule
import model.toPlayer
import model.toVariante
import model.variantes

const val FAVOURITES = "favourites"

class UnreachableFavouritesException : Exception()
class FavFirebase(private val db: FirebaseFirestore) : Fav {

    private var state: FavState = Idle

    override suspend fun select(info: GameInfo): Game {
        val snapshot = db.collection(FAVOURITES).whereEqualTo("title", info.title).get().await()
        val board = Board.fromMovesList(
            title = info.title,
            turn =  snapshot.documents[0].get("turn").toPlayers(),
            variantes = snapshot.documents[0].get("variant").toVariantes(),
            openingrule = snapshot.documents[0].get("openingrule").toOpeningRules(),
            moves = snapshot.documents[0].get("board").toMoves(),
            )
        return Game(board = board)
    }


    fun QueryDocumentSnapshot.toGameInfo():GameInfo{
        return GameInfo(
            title = this.get("title") as String,
            opponent = this.get("opponent") as String,
            date = this.get("date") as Timestamp,
            time = this.get("time") as Timestamp
        )
    }
    override suspend fun getFavourites(): List<GameInfo> {
        try {
            val snapshot = db.collection(FAVOURITES).get().await()
            return snapshot.toGameList()
        }
        catch (e: Throwable) {
            throw UnreachableFavouritesException()
        }
    }

    private fun subscribeRosterUpdated(flow: ProducerScope<FavEvent>) =
        db.collection(FAVOURITES).addSnapshotListener { snapshot, error ->
            when {
                error != null -> flow.close(error)
                snapshot != null -> flow.trySend(RosterUpdated(snapshot.toGameList().map { it }))
            }
        }

    override suspend fun enter(favGame: GameInfo): List<GameInfo> {
        check(state == Idle)
        try {
            return getFavourites()
        }
        catch (e: Throwable) {
            throw UnreachableFavouritesException()
        }
    }

    override fun enterAndObserve(): Flow<FavEvent> {
        check(state == Idle)
        return callbackFlow {
            state = InUseWithFlow( scope = this)

            var rosterSubscription: ListenerRegistration? = null
            try {

                rosterSubscription = subscribeRosterUpdated(flow = this)
            }
            catch (e: Exception) {
                close(e)
            }

            awaitClose {
                rosterSubscription?.remove()
            }
        }
    }

    override suspend fun leave() {
        when (val currentState = state) {
            is InUseWithFlow -> currentState.scope.close()
            is InUse -> currentState.localPlayerDocRef.delete().await()
            else -> state = Idle
            //is Idle -> throw IllegalStateException()
        }
        state = Idle
    }
    fun QuerySnapshot.toGameList() = map { it.toGameInfo() }
}

private fun Any?.toPlayers(): Player {
    return this.toString().toPlayer()
}

private fun Any?.toVariantes(): variantes {
    return this.toString().toVariante()
}

private fun Any?.toOpeningRules(): openingrule {
    return this.toString().toOpeningRule()
}

private fun Any?.toMoves(): List<String> {
    return this.toString().toMovesList()
}






private sealed class FavState
private class InUse(
    val localPlayerDocRef: DocumentReference
): FavState()

private class InUseWithFlow(
    val scope: ProducerScope<FavEvent>
) : FavState()

private object Idle : FavState()