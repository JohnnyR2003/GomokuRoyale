package model

import kotlin.math.abs

/**
 * Represents a Tic-Tac-Toe board. Instances are immutable.
 * @property turn   The next player to move
 * @property moves  The board moves
 */
data class Board(
    val turn: Player = Player.firstToMove,
    val variantes: variantes =model.variantes.NORMAL,
    val openingrule: openingrule = model.openingrule.PRO,
    val boardSize :Int = 15,
    val moves :moves = emptyMap()
    /*val tiles: List<List<Piece?>> =
        List(
            size = BOARD_SIDE,
            init = { List(size = BOARD_SIDE, init = { null }) }
        )*/
) {

    companion object {
        fun fromMovesList(turn: Player,variantes: variantes,openingrule: openingrule, moves: List<String>) = Board(
            turn = turn,
            variantes,
            openingrule,
            if (variantes ==model.variantes.OMOK)19 else 15,
            moves = buildMap {
                var i =0
                //val moves = emptyMap<Cell,Player>().toMutableMap()
                while (i < moves.size){//1A 10A

                    val pairParts = moves[i].split(':')
                    val rowNumbers =  pairParts[0].getUntilLetter()
                    val row = rowNumbers.toInt() -1
                    val column = if (pairParts[0][1].isDigit())pairParts[0][2].code - 'A'.code else pairParts[0][1].code - 'A'.code
                    val coordinate = Coordinate(row,column,if (variantes ==model.variantes.OMOK)19 else 15)
                    this.put(coordinate ,(Player.fromChar(pairParts[1][0])))
                    i += 1
                }
            }
        )
        private fun String.getUntilLetter() :String{
            var a = ""
            for (i in this){
                if (i.code < 'A'.code || i.code > 'Z'.code){
                    a += i
                }
            }
            return a
        }
    }


    /**
     * Overloads the indexing operator
     */
    operator fun get(at: Coordinate): Player? = getMove(at)

    /**
     * Gets the move at the given coordinates.
     * @param at    the move's coordinates
     * @return the [Marker] instance that made the move, or null if the position is empty
     */
    fun getMove(at: Coordinate): Player? = moves[at]//tiles[at.row][at.column]

    /**
     * Makes a move at the given coordinates and returns the new board instance.
     * @param at    the board's coordinate
     * @throws IllegalArgumentException if the position is already occupied
     * @return the new board instance
     */
    fun makeMove(at: Coordinate): Board {
        check(this[at] == null)
        check(openingRuleVerify(at))
        return Board(
            turn = turn.other(),
            variantes,
            openingrule,
            boardSize = if (variantes == model.variantes.OMOK)19 else 15,
            moves = moves + Pair(at,turn)
        )
    }

    private fun openingRuleVerify( at: Coordinate): Boolean {
        when(openingrule){
            model.openingrule.PRO ->{
                val elementCount = countBoardElementes()
                if (elementCount==0){
                    val middleIndex = boardSize /2
                    return at == Coordinate(middleIndex,middleIndex,boardSize)
                }else if (elementCount ==2){
                    val middleCoordinate = Coordinate(boardSize/2,boardSize/2,boardSize)
                    return distanceBetweenPieces(middleCoordinate,at) >= 3
                }else return true
            }
            model.openingrule.LONGPRO->{
                val elementCount = countBoardElementes()
                if (elementCount==0){
                    val middleIndex = boardSize/2
                    return at == Coordinate(middleIndex,middleIndex,boardSize)
                }else if (elementCount ==2){
                    val middleCoordinate = Coordinate(boardSize/2,boardSize/2,boardSize)
                    return distanceBetweenPieces(middleCoordinate,at) >= 4
                }else return true
            }
        }
    }
    private fun distanceBetweenPieces(firstCoordinate: Coordinate,secondCoordinate: Coordinate):Int {
        val rowdist = abs(firstCoordinate.row - secondCoordinate.row)
        val coldist = abs(firstCoordinate.column - secondCoordinate.column)
        return if (rowdist>=coldist) rowdist else coldist
    }

    private fun countBoardElementes():Int=moves.size

    /**
     * Converts this instance to a list of moves.
     */
    fun toMovesList(): List<String> = if (moves.isEmpty()) emptyList() else moves.map { it
        ->""+ (it.key.row + 1) + ('A' + it.key.column) +':'+ it.value.char
    }


}

/**
 * Extension function that checks whether this board represents a tied game or not
 * @return true if the board is a tied game, false otherwise
 */
fun Board.isTied(): Boolean = moves.size == boardSize*boardSize && !hasWon(Player.WHITE) && !hasWon(
    Player.BLACK)
//toMovesList().all { it != null } && !hasWon(Piece.WHITE) && !hasWon(Piece.BLACK)

/**
 * Extension function that checks whether the given marker has won the game
 * @return true if the player with the given marker has won, false otherwise
 */
fun Board.hasWon(player: Player): Boolean =       /**trocar e adicionar variante e abertura*/
    when(variantes){
        model.variantes.NORMAL -> boardIsWin(this,player,5)
        model.variantes.OMOK -> boardIsWin(this,player,3)
    }
private fun boardIsWin(board: Board, player: Player, nOfPiecesToWin:Int):Boolean{
    val directions = listOf(
        Pair(1, 0),  // horizontal
        Pair(0, 1),  // vertical
        Pair(1, 1),  // diagonal (top-left to bottom-right)
        Pair(1, -1)  // diagonal (top-right to bottom-left)
    )
    for (r in 0 until board.boardSize){
        for (c in 0 until board.boardSize){
            val lastCoordinate = Coordinate(r,c,board.boardSize)
            if (board.moves[lastCoordinate] == null) continue
            // Check one direction
            for ((dx, dy) in directions) {
                var count = 1
                // Check one direction
                for (i in 1 until nOfPiecesToWin) {
                    val row = lastCoordinate.row + i * dx
                    val col = lastCoordinate.column + i * dy
                    if (row < 0 || col < 0 || row >= board.boardSize || col >= board.boardSize) {
                        break
                    }
                    val currentCoordinate = Coordinate(row,col, board.boardSize)
                    if (board.moves[currentCoordinate] == player) {
                        count++
                        if (count == nOfPiecesToWin) {
                            return true
                        }
                    } else {
                        break
                    }
                }
                // Check the opposite direction
                for (i in 1 until nOfPiecesToWin) {
                    val row = lastCoordinate.row - i * dx
                    val col = lastCoordinate.column - i * dy
                    if (row < 0 || col < 0 || row >= board.boardSize || col >= board.boardSize) {
                        break
                    }
                    val currentCoordinate = Coordinate(row,col,board.boardSize)
                    if (board.moves[currentCoordinate] == player) {
                        count++
                        if (count == nOfPiecesToWin) {
                            return true
                        }
                    } else {
                        break
                    }
                }
            }
        }
    }
    return false
}

open class BoardResult
class HasWinner(val winner: Player) : BoardResult()
class Tied : BoardResult()
class OnGoing : BoardResult()

/**
 * Gets the current result of this board.
 */
fun Board.getResult(): BoardResult =
    when {
        hasWon(Player.BLACK) -> HasWinner(Player.BLACK)
        hasWon(Player.WHITE) -> HasWinner(Player.WHITE)
        moves.size == boardSize* boardSize -> Tied()

        else -> OnGoing()
    }