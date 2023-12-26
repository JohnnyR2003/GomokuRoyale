package model

/*
/*package com.example.helloworld

const val CHAR_TO_INDEX = 65

class Column private constructor(val symbol: Char) { // letter: Char
    val index: Int get() = symbol.toIndex()
    override fun toString(): String = "Column $symbol"

    companion object {
        val values = List(BOARD_DIM) { Column((it + CHAR_TO_INDEX).toChar()) }
        val INVALID = Column('?')
        operator fun invoke(col: Char): Column {
            require(col in CharRange('A', (BOARD_DIM + 64).toChar())) { INVALID }
            return values[col.toIndex()]
        }
    }
}

fun Char.toIndex() = this.code - CHAR_TO_INDEX

fun Char.toColumnOrNull(): Column? =
    if (this in CharRange('A', (BOARD_DIM + 64).toChar())) Column.values[this.toIndex()] else null

fun Char.toColumn(): Column = requireNotNull(toColumnOrNull()) { "Invalid column $this" }

*/
/*
class Column private constructor(val char: Char,val boardSize:Int) {
    val symbol = char
    val index = char - 'A'
    override fun toString() = "Column $char"
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is Column) {
            return false
        }
        return this.char == other.char && this.index == other.index
    }

    companion object {
        // Função que retorna o tamanho da coluna com base na variante
        fun getColumnSize(variante: variantes): Int {
            return when (variante) {
                variantes.OMOK -> 19
                variantes.NORMAL -> 15
            }
        }

        val values = List(getColumnSize(variantes.OMOK)) { Column('A' + it) }

        operator fun invoke(char: Char) = run {
            require(char in 'A'..'A' + getColumnSize(variantes.OMOK)) { throw IllegalArgumentException("Invalid column $char") }
            values[(char - 'A')]
        }
    }
}
*/
const val CHAR_TO_INDEX = 65

class Column private constructor(val symbol: Char) {
    val index: Int get() = symbol.toIndex()
    override fun toString(): String = "Column $symbol"

    companion object {
        val values = List(BOARD_SIZE) { Column((it + CHAR_TO_INDEX).toChar()) }
        val INVALID = Column('?')
        operator fun invoke(col: Char): Column {
            require(col in CharRange('A', (BOARD_SIZE + 64).toChar())) { INVALID }
            return values[col.toIndex()]
        }
    }
}

fun Char.toIndex() = this.code - CHAR_TO_INDEX

fun Char.toColumnOrNull(): Column? = if (this in 'A'..'H') Column.values[this - 'A'] else null
fun Char.toColumn(): Column = run {
    require(this in 'A'..'T') { throw IllegalArgumentException("Invalid column $this") }
    Column.values[this - 'A']
}

 */