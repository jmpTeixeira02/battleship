package isel.pdm.game.prep.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class TypeOfShip(val size: Int) {
    Destroyer(2),
    Submarine(3),
    Cruiser(3),
    BattleShip(4),
    Carrier(5)
}

@Parcelize
@kotlinx.serialization.Serializable
data class Ship(val type: TypeOfShip) : Parcelable {
    val size = type.size
    val name = type.name
}