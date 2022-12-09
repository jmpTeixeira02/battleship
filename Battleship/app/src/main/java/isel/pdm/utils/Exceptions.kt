package isel.pdm.utils

class InvalidOrientationException(val msg: String = ""): Exception(msg)

class CellIsAlreadyOccupiedException(val msg: String = ""): Exception(msg)