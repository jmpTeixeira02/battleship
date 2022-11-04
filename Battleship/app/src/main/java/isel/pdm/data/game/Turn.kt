package isel.pdm.data.game

enum class TurnUser(val type: Char) {
    Enemy('E'),
    Player('P')
}

data class Turn(val user: TurnUser, val coords: Coordinates) {
    companion object {
        fun fromString(turn: String) : Turn {
            return Turn(TurnUser.valueOf(turn[0].toString()), Coordinates.fromString(turn.substring(1)))
        }
    }

    override fun toString(): String {
        return user.type.toString() + coords
    }
}