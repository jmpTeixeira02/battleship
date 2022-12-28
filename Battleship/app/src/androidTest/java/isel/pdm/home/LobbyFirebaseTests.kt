package isel.pdm.home

import isel.pdm.game.lobby.model.*
import isel.pdm.localTestPlayer
import isel.pdm.otherTestPlayersInLobby
import isel.pdm.testutils.PopulatedFirebaseLobby
import isel.pdm.testutils.SuspendingGate
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test


// In order to run there needs to be a local firebase instance running
// To do this install firebase emulator, select emulators with firestore and database
// firebase init; firebase emulators:start
@ExperimentalCoroutinesApi
class LobbyFirebaseTests {

    @get:Rule
    val populatedLobbyRule = PopulatedFirebaseLobby()

    @Test
    fun getPlayers_returns_players_in_the_lobby(): Unit = runTest {
        // Arrange
        val sut = populatedLobbyRule.lobby

        // Act
        val playersInLobby = sut.getPlayers()

        // Assert
        val expectedInLobby = otherTestPlayersInLobby
        Assert.assertTrue(playersInLobby.containsAll(expectedInLobby))
        Assert.assertTrue(expectedInLobby.size == playersInLobby.size)
    }

    @Test
    fun enter_places_player_in_the_lobby(): Unit = runTest {
        // Arrange
        val sut = populatedLobbyRule.lobby

        // Act
        sut.enter(localTestPlayer)

        // Assert
        val playersInLobby = sut.getPlayers()
        val expectedInLobby = otherTestPlayersInLobby + localTestPlayer
        Assert.assertTrue(playersInLobby.containsAll(expectedInLobby))
        Assert.assertTrue(expectedInLobby.size == playersInLobby.size)

        sut.leave()
    }

    @Test
    fun leave_removes_player_from_the_lobby(): Unit = runTest {
        // Arrange
        val sut = populatedLobbyRule.lobby
        sut.enter(localTestPlayer)
        val playersInLobbyAfterEnter = sut.getPlayers()

        // Act
        sut.leave()
        val playersInLobbyAfterLeave = sut.getPlayers()

        // Assert
        Assert.assertTrue(playersInLobbyAfterEnter.contains(localTestPlayer))
        Assert.assertFalse(playersInLobbyAfterLeave.contains(localTestPlayer))
    }

    @Test
    fun enterAndObserve_flow_reports_roster_changes(): Unit = runTest {
        // Arrange
        val sut = populatedLobbyRule.lobby
        val expectedCount = otherTestPlayersInLobby.size + 1
        val observations: MutableList<List<PlayerInfo>> = mutableListOf()
        val canDelete = SuspendingGate()
        val hasDeleted = SuspendingGate()

        // Act
        val collectJob = launch {
            sut.enterAndObserve(localTestPlayer).collect { event ->
                if (event is RosterUpdated) {
                    when (event.players.size) {
                        expectedCount -> {
                            canDelete.open()
                            observations.add(event.players)
                        }
                        expectedCount-1 -> {
                            hasDeleted.open()
                            observations.add(event.players)
                        }
                    }
                }
            }
        }

        canDelete.await()

        populatedLobbyRule.app.emulatedFirestoreDb
            .collection(LOBBY)
            .document(otherTestPlayersInLobby.first().id.toString())
            .delete()
            .await()

        hasDeleted.await()
        sut.leave()
        collectJob.join()

        // Assert
        Assert.assertEquals(2, observations.size)
        Assert.assertEquals(otherTestPlayersInLobby.size + 1, observations.first().size)
        Assert.assertTrue(observations.first().contains(localTestPlayer))

        Assert.assertEquals(otherTestPlayersInLobby.size, observations[1].size)
        Assert.assertTrue(observations.first().contains(localTestPlayer))
    }

    @Test
    fun enterAndObserve_flow_reports_received_challenges(): Unit = runTest {
        // Arrange
        val sut = populatedLobbyRule.lobby
        val enteredLobby = SuspendingGate()
        val challengeReceived = SuspendingGate()
        var received: PlayerInfo? = null

        // Act
        val collectJob = launch {
            sut.enterAndObserve(localTestPlayer).collect { event ->
                if (event is RosterUpdated) {
                    if (event.players.any { it == localTestPlayer })
                        enteredLobby.open()
                }
                if (event is ChallengeReceived) {
                    received = event.challenge.challenger
                    challengeReceived.open()
                }
            }
        }

        enteredLobby.await()

        val challenger = PlayerInfo("challengerNick")

        populatedLobbyRule.app.emulatedFirestoreDb
            .collection(LOBBY)
            .document(localTestPlayer.id.toString())
            .update(CHALLENGER_FIELD, challenger.toDocumentContent())
            .await()

        challengeReceived.await()
        sut.leave()
        collectJob.join()

        // Assert
        assertEquals(challenger, received)
    }

    @Test
    fun issueChallenge_removes_challenger_from_the_lobby(): Unit = runTest {
        // Arrange
        val sut = populatedLobbyRule.lobby
        sut.enter(localTestPlayer)

        // Act
        sut.issueChallenge(otherTestPlayersInLobby.first())

        // Assert
        val stillInLobby = sut.getPlayers().any { it == localTestPlayer }
        Assert.assertFalse(stillInLobby)
    }

    @Test
    fun issueChallenge_updates_challenged_data_with_challenger_info(): Unit = runTest {
        // Arrange
        val sut = populatedLobbyRule.lobby
        sut.enter(localTestPlayer)

        // Act
        val challengedPLayer = otherTestPlayersInLobby.first()
        sut.issueChallenge(challengedPLayer)

        // Assert
        val challenge = populatedLobbyRule.app.emulatedFirestoreDb
            .collection(LOBBY)
            .document(challengedPLayer.id.toString())
            .get()
            .await()
            .toChallengeOrNull()

        Assert.assertNotNull(challenge)
        assertEquals(localTestPlayer, challenge?.challenger)
        assertEquals(challengedPLayer, challenge?.challenged)
    }

    @Test
    fun issueChallenge_returns_match_participants_info(): Unit = runTest {
        // Arrange
        val sut = populatedLobbyRule.lobby
        sut.enter(localTestPlayer)

        // Act
        val match = sut.issueChallenge(otherTestPlayersInLobby.first())

        // Assert
        assertEquals(localTestPlayer, match.challenger)
        assertEquals(otherTestPlayersInLobby.first(), match.challenged)
    }

    @Test(expected = IllegalStateException::class)
    fun issueChallenge_without_entering_lobby_throws(): Unit = runTest {
        val sut = populatedLobbyRule.lobby
        sut.issueChallenge(otherTestPlayersInLobby.first())
    }

    @Test(expected = IllegalStateException::class)
    fun leave_without_entering_lobby_throws(): Unit = runTest {
        val sut = populatedLobbyRule.lobby
        sut.leave()
    }

    @Test(expected = IllegalStateException::class)
    fun enter_without_leaving_lobby_throws(): Unit = runTest {
        val sut = populatedLobbyRule.lobby
        sut.enter(localTestPlayer)
        sut.enter(localTestPlayer)
    }
}


