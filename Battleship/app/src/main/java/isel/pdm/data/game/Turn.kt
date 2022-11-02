package isel.pdm.data.game

enum class TurnUser(val type: Char) {
    Enemy('E'),
    Player('P')
}

data class Turn(val user: TurnUser, val coords: Coordinates) {
    override fun toString(): String {
        return user.type + coords
    }
}