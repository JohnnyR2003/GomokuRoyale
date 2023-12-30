package model

import kotlinx.android.parcel.RawValue


typealias moves = @RawValue Map<Coordinate, Player>



const val GAME_SIZE = 225
const val OMOK_DIM = 19

enum class Player(val char: Char)  {
    BLACK('B'),
    WHITE('W');


    fun other() = when (this) {
        BLACK -> WHITE
        WHITE -> BLACK


    }

    fun toChar() = when (this) {
        BLACK -> 'B'
        WHITE -> 'W'

    }



    companion object {
        val firstToMove: Player = BLACK
        fun fromChar(c: Char) = when (c) {
            'B' -> BLACK
            'W' -> WHITE
            else -> throw IllegalArgumentException("Invalid character for player.")
        }
    }
}
fun String.toPlayer() = when (this) {
    "BLACK" -> Player.BLACK
    "WHITE" -> Player.WHITE
    else -> throw IllegalArgumentException("Invalid player.")
}