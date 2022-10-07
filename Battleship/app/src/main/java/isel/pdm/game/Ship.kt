abstract data class Ship(val type: KClass<? super Ship>, val size: Int, val start: Coordinates, val end: Coordinates) {
    protected var hasDrowned: Boolean
        set(value) {
            if (value)
                Crash()
        }
    protected var nHits: Int = 0
        set(value) {
            field = value
            hasDrowned = value == Size
        }

    abstract fun Crash()
}