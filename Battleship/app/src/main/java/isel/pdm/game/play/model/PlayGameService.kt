package isel.pdm.game.play.model

import android.os.Parcelable
import isel.pdm.game.lobby.model.PlayerMatchmaking
import isel.pdm.game.prep.model.*
import kotlinx.coroutines.delay
import kotlinx.parcelize.Parcelize

interface PlayGameService {
    suspend fun fakePlay(): Coordinate
}

@Parcelize
data class FakeOpponent(
    val fakeUser: PlayerMatchmaking,
    val fakePrepBoard: Board,
) : Parcelable

class FakeOpponentService : PlayGameService {

    private var fakePrepBoard: Board = Board()

    private fun addShipsToFakePrepBoard(): Board {
        fakePrepBoard.randomFleet()
        return fakePrepBoard
    }


    val opponent = FakeOpponent(
        fakeUser = PlayerMatchmaking("fake"),
        fakePrepBoard = addShipsToFakePrepBoard(),
    )


    override suspend fun fakePlay(): Coordinate = generateRandomCoordinate()


    private fun generateRandomCoordinate(): Coordinate =
        Coordinate((0..9).random(), (0..9).random())

}