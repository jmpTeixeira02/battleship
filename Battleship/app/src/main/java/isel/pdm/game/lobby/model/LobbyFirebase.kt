package isel.pdm.game.lobby.model

import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.*

class UnreachableLobbyException : Exception()

/**
 * Implementation of the Game's lobby using Firebase's Firestore
 */
class LobbyFirebase(private val db: FirebaseFirestore) : Lobby {

    private var state: LobbyState = Idle

    private suspend fun addLocalPlayer(localPlayer: PlayerInfo): DocumentReference {
        val docRef = db.collection(LOBBY).document(localPlayer.id.toString())
        docRef
            .set(localPlayer.toDocumentContent())
            .await()
        return docRef
    }

    private fun subscribeRoasterUpdated(flow: ProducerScope<LobbyEvent>) =
        db.collection(LOBBY).addSnapshotListener { snapshot, error ->
            when {
                error != null -> flow.close(error)
                snapshot != null -> flow.trySend(RosterUpdated(snapshot.toPlayerList()))
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
                if (challenge != null)
                    flow.trySend(ChallengeReceived(challenge))
            }
        }
    }

    override suspend fun getPlayers(): List<PlayerInfo> {
        try {
            val result = db.collection(LOBBY).get().await()
            return result.map { it.toPlayerInfo() }
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
            addLocalPlayer(localPlayer)
            try {
                myDocRef = addLocalPlayer(localPlayer)
                challengeSubscription = subscribeChallengeReceived(myDocRef, flow = this)
                rosterSubscription = subscribeRoasterUpdated(flow = this)
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

        leave()
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
const val NICK_FIELD = "nick"
const val CHALLENGER_FIELD = "challenger"
const val CHALLENGER_ID_FIELD = "id"

/**
 * Extension function used to convert player info documents stored in the Firestore DB
 * into [PlayerInfo] instances.
 */
fun QueryDocumentSnapshot.toPlayerInfo()=
    PlayerInfo(
        username = data[NICK_FIELD] as String,
        id = UUID.fromString(id),
    )



/**
 * [PlayerInfo] extension function used to convert an instance to a map of key-value
 * pairs containing the object's properties
 */
fun PlayerInfo.toDocumentContent() = mapOf(
    NICK_FIELD to username,
    CHALLENGER_ID_FIELD to id.toString()
)

@Suppress("UNCHECKED_CAST")
fun DocumentSnapshot.toChallengeOrNull(): Challenge? {
    val docData = data
    if (docData != null) {
        val challenger = docData[CHALLENGER_FIELD] as Map<String, String>?
        if (challenger != null) {
            return Challenge(
                challenger = playerInfoFromDocContent(challenger),
                challenged = this.toPlayerInfo()
            )
        }
    }
    return null
}

fun DocumentSnapshot.toPlayerInfo() = PlayerInfo(
    username = data?.get(NICK_FIELD) as String,
    id = UUID.fromString(id)
)


fun playerInfoFromDocContent(properties: Map<String, Any>) = PlayerInfo(
    username  = properties[NICK_FIELD] as String,
    id = UUID.fromString(properties[CHALLENGER_ID_FIELD] as String)
)

fun QuerySnapshot.toPlayerList() = map { it.toPlayerInfo() }
