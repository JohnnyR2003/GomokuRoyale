package model
/*
import ui.BOARD_SIZE

/*package com.example.helloworld

class Row private constructor(val number: Int) {
    val index: Int get() = number - 1

    override fun toString(): String = "Row $number"
    companion object {
        val values = List(BOARD_DIM) { Row(it + 1) }
        val INVALID = Row(0)
        operator fun invoke(r: Int): Row {
            require(r in 1 .. BOARD_DIM) { INVALID }
            return values[r - 1]
        }
    }
}

fun Int.toRowOrNull(): Row? = if (this in 1..BOARD_DIM) Row.values[this - 1] else null

fun Int.toRow(): Row = requireNotNull(toRowOrNull()) { "Invalid row $this" }
*/

/*
class Row private constructor(val n: Int) {
    val number = n
    val index = n - 1
    override fun toString() = "Row $n"
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is Row) {
            return false
        }
        return this.n == other.n && this.index == other.index
    }

    companion object {
        // Função que retorna o tamanho do tabuleiro com base na variante
        fun getBoardSize(variante: variantes): Int {
            return when (variante) {
                variantes.OMOK -> 19
                variantes.NORMAL -> 15
            }
        }

        val values = List(getBoardSize(variantes.OMOK)) { Row(it + 1) }

        operator fun invoke(n: Int) = run {
            require(n in 1..getBoardSize(variantes.OMOK)) { throw IllegalStateException("Invalid row $n") }
            values[n - 1]
        }
    }
}*/
class Row private constructor(val number: Int) {
    val index: Int get() = number - 1

    override fun toString(): String = "Row $number"
    companion object {
        val values = List(15) { Row(it + 1) } //ver como colocar aqui
        val valuesOMOK = List(19){Row(it + 1)}
        val INVALID = Row(0)
        operator fun invoke(r: Int): Row {
            require(r in 1 .. BOARD_SIZE) { INVALID }
            return values[r - 1]
        }
    }
}
fun Int.toRowOrNull(): Row? = if(BOARD_SIZE == 15) {if (this in 1..BOARD_SIZE) Row.values[this - 1] else null}
                                else{
                                    if (this in 1..BOARD_SIZE) Row.valuesOMOK[this - 1] else null
                                }
fun Int.toRow(): Row = run {
    require(this in 1..BOARD_SIZE) { throw IllegalStateException("Invalid row $this") }
    if (BOARD_SIZE ==15){
        Row.values[this - 1]
    }else Row.valuesOMOK[this - 1]
}


 */