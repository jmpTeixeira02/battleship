package isel.pdm.game.play.model

import android.os.Parcelable
import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.game.prep.model.Board
import isel.pdm.game.prep.model.Coordinate
import kotlinx.parcelize.Parcelize

interface PlayGameService {
    suspend fun fakePlay(): Coordinate
}

@Parcelize
data class FakeOpponent(
    val fakeUser: PlayerInfo,
    val fakePrepBoard: Board,
) : Parcelable

class FakeOpponentService : PlayGameService {

    private var fakePrepBoard: Board = Board()

    private fun addShipsToFakePrepBoard(): Board {
        fakePrepBoard.randomFleet()
        return fakePrepBoard
    }


    val opponent = FakeOpponent(
        fakeUser = PlayerInfo("fake"),
        fakePrepBoard = addShipsToFakePrepBoard(),
    )


    override suspend fun fakePlay(): Coordinate = generateRandomCoordinate()


    private fun generateRandomCoordinate(): Coordinate =
        Coordinate((0..9).random(), (0..9).random())

}