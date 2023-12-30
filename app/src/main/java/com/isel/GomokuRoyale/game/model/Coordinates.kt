package model

/**
 * The Tic-Tac-Toe's board side
 */
//const val BOARD_SIDE = 3

/**
 * Represents coordinates in the Gomoku board
 */
data class Coordinate(val row: Int, val column: Int,val boardSize:Int) {
    init {
        require(isValidRow(row,boardSize) && isValidColumn(column,boardSize))
    }
}

/**
 * Checks whether [value] is a valid row index
 */
fun isValidRow(value: Int,boardSize:Int) = value in 0 until boardSize

/**
 * Checks whether [value] is a valid column index
 */
fun isValidColumn(value: Int,boardSize:Int) = value in 0 until boardSize

