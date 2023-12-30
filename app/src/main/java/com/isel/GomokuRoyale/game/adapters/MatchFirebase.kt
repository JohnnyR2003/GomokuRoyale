package com.isel.GomokuRoyale.game.adapters

import android.icu.text.LocaleDisplayNames.UiListItem
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.isel.GomokuRoyale.Favourites.GameInfo
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import model.Board
import model.Coordinate
import model.Game
import model.GameEnded
import model.GameEvent
import model.GameStarted
import model.Match
import model.MoveMade
import model.Player
import model.getLocalPlayerMarker
import model.makeMove
import model.toOpeningRule
import model.toVariante
import model.variantes
import java.util.UUID

class MatchFirebase(private val db: FirebaseFirestore) : Match {

    private var onGoingGame: Pair<Game, String>? = null
    private var player = Player.firstToMove
    private fun subscribeGameStateUpdated(
        localPlayerMarker: Player,
        gameId: String,
        flow: ProducerScope<GameEvent>
    ) =
        db.collection(ONGOING)
            .document(gameId)
            .addSnapshotListener { snapshot, error ->
                when {
                    error != null -> flow.close(error)
                    snapshot != null -> {
                        snapshot.toMatchStateOrNull()?.let {
                            /*val game =Game(
                                localPlayer = player,
                                forfeitedBy = it.second,
                                board = it.first
                            )*/
                            val game =if (it.first.moves.size %2 == 0){
                                Game(
                                    localPlayer = localPlayerMarker,
                                    forfeitedBy = it.second,
                                    board = it.first
                                )
                            }else {
                                Game(
                                    localPlayer = localPlayerMarker.other(),
                                    forfeitedBy = it.second,
                                    board = it.first
                                )}

                            val gameEvent = when {
                                onGoingGame == null -> GameStarted(game)
                                game.forfeitedBy != null -> GameEnded(game, game.forfeitedBy.other())
                                else -> MoveMade(game)
                            }
                            onGoingGame = Pair(game, gameId)

                            flow.trySend(gameEvent)
                        }
                    }
                }
            }

    private suspend fun publishGame(game: Game, gameId: String) {
        db.collection(ONGOING)
            .document(gameId)
            .set(game.board.toDocumentContent())
            .await()
    }

    private suspend fun updateGame(game: Game, gameId: String) {
        db.collection(ONGOING)
            .document(gameId)
            .update(game.board.toDocumentContent())
            .await()
    }
    //saves a specific game from the collection ONGOING in a new collection called favourites
    private suspend fun saveGame(game: Game, gameId: String) {
        db.collection("favourites")
            .document(gameId)
            .set(game.board.toDocumentContent())
            .await()
    }

    /*
        override fun getFavourites(): List<GameInfo> {
            val gameList = mutableListOf<GameInfo>()
            db.collection("favourites")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val gameid = document.id
                        gameList.add(GameInfo(
                            title = gameid,
                            opponent = document.getString("turn").toString(),
                            date = document.getDate("date").toString(),
                            time = document.getDate("time").toString())
                        )
                    }
                }
            return gameList
        }
       override fun getFavourites(): Flow<List<GameInfo>> {
            return callbackFlow {
                val gameSubscription = db.collection("favourites")
                    .addSnapshotListener { snapshot, error ->
                        when {
                            error != null -> close(error)
                            snapshot != null -> {
                                val gameid = snapshot.documents.mapNotNull { it.id }
                                val gameDate = snapshot.documents.mapNotNull { it.getDate(gameid.toString()) }
                                val gameTime = snapshot.documents.mapNotNull { it.getTimestamp(gameid.toString()) }
                                val gameList = snapshot.documents.mapNotNull { it.toMatchStateOrNull() }
                                    .map { GameInfo(
                                        title = gameid.toString(),
                                        opponent = it.first.turn.other().name,
                                        date = gameDate.toString(),
                                        time = gameTime.toString(),

                                    ) }
                                trySend(gameList)
                            }
                        }
                    }
                awaitClose {
                    gameSubscription.remove()
                }
            }
        }*/



    override fun start(localPlayer: Player, gameIds: UUID, board: Board): Flow<GameEvent> {
        check(onGoingGame == null)

        return callbackFlow {
            val newGame = Game(
                localPlayer = getLocalPlayerMarker(localPlayer),
                board = board
            )
            val gameId = gameIds.toString()

            var gameSubscription: ListenerRegistration? = null
            try {
                publishGame(newGame, gameId)
                saveGame(newGame, gameId)



                gameSubscription = subscribeGameStateUpdated(
                    localPlayerMarker = newGame.localPlayer,
                    gameId = gameId,
                    flow = this
                )
            } catch (e: Throwable) {
                close(e)
            }

            awaitClose {
                gameSubscription?.remove()
            }
        }
    }

    override suspend fun makeMove(at: Coordinate) {
        onGoingGame = checkNotNull(onGoingGame).let {//.also
            val game = it.copy(first = it.first.makeMove(at))
            updateGame(game.first, game.second)
            saveGame(game.first, game.second)
            player = player.other()
            return@let game
        }
    }

    override suspend fun forfeit() {
        onGoingGame = checkNotNull(onGoingGame).also {
            db.collection(ONGOING)
                .document(it.second)
                .update(FORFEIT_FIELD, it.first.localPlayer)
                .await()
        }
    }

    override suspend fun end() {
        onGoingGame = checkNotNull(onGoingGame).let {
            db.collection(ONGOING)
                .document(it.second)
                .delete()
                .await()
            null
        }
    }
}

/**
 * Names of the fields used in the document representations.
 */
const val ONGOING = "ongoing"
const val TURN_FIELD = "turn"
const val BOARD_FIELD = "board"
const val FORFEIT_FIELD = "forfeit"
const val VARIANT_FIELD = "variant"
const val OPENINGRULE_FIELD = "openingrule"
const val DATE_FIELD = "date"
const val TIME_FIELD = "time"
const val OPPONENT_FIELD = "opponent"
const val TITLE_FIELD = "title"


/**
 * [Board] extension function used to convert an instance to a map of key-value
 * pairs containing the object's properties
 */
fun Board.toDocumentContent() = mapOf(
    TURN_FIELD to turn.name,
    BOARD_FIELD to toMovesList().joinToString(separator = ","),
    OPENINGRULE_FIELD to openingrule.name,
    VARIANT_FIELD to variantes.name,
    DATE_FIELD to Timestamp.now(),
    TIME_FIELD to Timestamp.now(),
    OPPONENT_FIELD to turn.other().name,
    TITLE_FIELD to this.title,



)


/**
 * Extension function to convert documents stored in the Firestore DB
 * into the corresponding match state.
 */
fun DocumentSnapshot.toMatchStateOrNull(): Pair<Board, Player?>? =
    data?.let {
        val openingrule = it[OPENINGRULE_FIELD].toString().toOpeningRule()
        val variante = it[VARIANT_FIELD].toString().toVariante()
        val title = it[TITLE_FIELD].toString()
        val moves = it[BOARD_FIELD] as String
        val turn = Player.valueOf(it[TURN_FIELD] as String)
        val forfeit = it[FORFEIT_FIELD] as String?
        Pair(
            first = Board.fromMovesList(title = title,turn = turn, variantes = variante, openingrule = openingrule, moves = if (moves.isEmpty()) emptyList() else moves.toMovesList()),
            second =  if (forfeit != null) Player.valueOf(forfeit) else null
        )
    }

/**
 * Converts this string to a list of moves in the board
 */
fun String.toMovesList(): List<String> = this.split(",")
