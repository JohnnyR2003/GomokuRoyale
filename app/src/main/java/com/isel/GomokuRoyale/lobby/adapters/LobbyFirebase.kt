package com.isel.GomokuRoyale.lobby.adapters

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.isel.GomokuRoyale.lobby.domain.Challenge
import com.isel.GomokuRoyale.lobby.domain.ChallengeReceived
import com.isel.GomokuRoyale.lobby.domain.Lobby
import com.isel.GomokuRoyale.lobby.domain.LobbyEvent
import com.isel.GomokuRoyale.lobby.domain.PlayerInfo
import com.isel.GomokuRoyale.lobby.domain.RosterUpdated
import com.isel.GomokuRoyale.preferences.model.UserInfo
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class UnreachableLobbyException : Exception()

/**
 * Implementation of the Game's lobby using Firebase's Firestore
 */
class LobbyFirebase(private val db: FirebaseFirestore) : Lobby {

    private var state: LobbyState = Idle

    private suspend fun addLocalPlayer(localPlayer: PlayerInfo): DocumentReference {
        val docRef = db.collection(LOBBY).document(localPlayer.id.toString())
        docRef
            .set(localPlayer.info.toDocumentContent())
            .await()
        return docRef
    }

    override fun deletePlayer(it: PlayerInfo) {
        db.collection(LOBBY).document(it.id.toString()).delete()
    }

    private fun subscribeRosterUpdated(flow: ProducerScope<LobbyEvent>) =
        db.collection(LOBBY).addSnapshotListener { snapshot, error ->
            when {
                error != null -> flow.close(error)
                snapshot != null -> flow.trySend(RosterUpdated(snapshot.toPlayerList().map { it!! }))
            }
        }

    private fun subscribeChallengeReceived(
        localPlayerDocRef: DocumentReference,
        flow: ProducerScope<LobbyEvent>
    ) = localPlayerDocRef.addSnapshotListener { snapshot, error ->
        when {
            error != null -> flow.close(error)
            snapshot != null -> {
                val challenge: Challenge? = snapshot.toChallengeOrNull()
                if (challenge != null) {
                    flow.trySend(ChallengeReceived(challenge))
                }
            }
        }
    }

    override suspend fun getPlayers(): List<PlayerInfo> {
        try {
            val result = db.collection(LOBBY).get().await()
            return result.map { it.toPlayerInfo()!! }
        }
        catch (e: Throwable) {
            throw UnreachableLobbyException()
        }
    }

    override suspend fun enter(localPlayer: PlayerInfo): List<PlayerInfo> {
        check(state == Idle)
        try {
            state = InUse(localPlayer, addLocalPlayer(localPlayer))
            return getPlayers()
        }
        catch (e: Throwable) {
            throw UnreachableLobbyException()
        }
    }

    override fun enterAndObserve(localPlayer: PlayerInfo): Flow<LobbyEvent> {
        check(state == Idle)
        return callbackFlow {
            state = InUseWithFlow(localPlayer, scope = this)

            var myDocRef: DocumentReference? = null
            var rosterSubscription: ListenerRegistration? = null
            var challengeSubscription: ListenerRegistration? = null
            try {
                myDocRef = addLocalPlayer(localPlayer)
                challengeSubscription = subscribeChallengeReceived(myDocRef, flow = this)
                rosterSubscription = subscribeRosterUpdated(flow = this)
            }
            catch (e: Exception) {
                close(e)
            }

            awaitClose {
                rosterSubscription?.remove()
                challengeSubscription?.remove()
                myDocRef?.delete()
            }
        }
    }

    override suspend fun issueChallenge(to: PlayerInfo): Challenge {
        val localPlayer = when (val currentState = state) {
            is Idle -> throw java.lang.IllegalStateException()
            is InUse -> currentState.localPlayer
            is InUseWithFlow -> currentState.localPlayer
        }

        db.collection(LOBBY)
            .document(to.id.toString())
            .update(CHALLENGER_FIELD, localPlayer.toDocumentContent())
            .await()

        return Challenge(challenger = localPlayer, challenged = to)
    }

    override suspend fun leave() {
        when (val currentState = state) {
            is InUseWithFlow -> currentState.scope.close()
            is InUse -> currentState.localPlayerDocRef.delete().await()
            is Idle -> throw IllegalStateException()
        }
        state = Idle
    }
}

/**
 * Sum type that characterizes the lobby state
 */
private sealed class LobbyState

private class InUse(
    val localPlayer: PlayerInfo,
    val localPlayerDocRef: DocumentReference
): LobbyState()

private class InUseWithFlow(
    val localPlayer: PlayerInfo,
    val scope: ProducerScope<LobbyEvent>
) : LobbyState()

private object Idle : LobbyState()

/**
 * Names of the fields used in the document representations.
 */
const val LOBBY = "lobby"
const val USERNAME_FIELD = "nick"
const val ID_FIELD = "id"
const val CHALLENGER_FIELD = "challenger"
const val CHALLENGER_ID_FIELD = "id"

/**
 * Extension function used to convert player info documents stored in the Firestore DB
 * into [PlayerInfo] instances.
 */
fun QueryDocumentSnapshot.toPlayerInfo() =
    (data[ID_FIELD] as String?)?.let {
        UserInfo(
            username = data[USERNAME_FIELD] as String,
            status = it
        )
    }?.let {
        PlayerInfo(
        info = it,
        id = UUID.fromString(id),
    )
    }

/**
 * [PlayerInfo] extension function used to convert an instance to a map of key-value
 * pairs containing the object's properties
 */
fun PlayerInfo.toDocumentContent() = mapOf(
    USERNAME_FIELD to info.username,
    ID_FIELD to info.status,
    CHALLENGER_ID_FIELD to id.toString()
)

@Suppress("UNCHECKED_CAST")
fun DocumentSnapshot.toChallengeOrNull(): Challenge? {
    val docData = data
    if (docData != null) {
        val challenger = docData[CHALLENGER_FIELD] as Map<String, String>?
        if (challenger != null) {
            return playerInfoFromDocContent(challenger)?.let {
                Challenge(
                    challenger = it,
                    challenged = this.toPlayerInfo()!!
                )
            }
        }
    }
    return null
}

fun DocumentSnapshot.toPlayerInfo() = (data?.get(ID_FIELD) as String?)?.let {
    UserInfo(
        username = data?.get(USERNAME_FIELD) as String,
        status = it
    )
}?.let {
    PlayerInfo(
        info = it,
        id = UUID.fromString(id)
    )
}


fun playerInfoFromDocContent(properties: Map<String, Any>) = (properties[ID_FIELD] as String?)?.let {
    UserInfo(
        username = properties[USERNAME_FIELD] as String,
        status = it,
    )
}?.let {
    PlayerInfo(
        info = it,
        id = UUID.fromString(properties[CHALLENGER_ID_FIELD] as String)
    )
}

fun QuerySnapshot.toPlayerList() = map { it.toPlayerInfo() }

/**
 * [UserInfo] extension function used to convert an instance to a map of key-value
 * pairs containing the object's properties
 */
fun UserInfo.toDocumentContent() = mapOf(
    USERNAME_FIELD to username,
    ID_FIELD to status
)