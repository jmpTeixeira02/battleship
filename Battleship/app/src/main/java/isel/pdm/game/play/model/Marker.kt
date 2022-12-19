package isel.pdm.game.play.model

enum class Marker {

    LOCAL, OPPONENT;

    companion object {
        val firstToMove: Marker = LOCAL
    }

    /**
     * The other player move
     */
    val other: Marker
        get() = if (this == LOCAL) OPPONENT else LOCAL
}