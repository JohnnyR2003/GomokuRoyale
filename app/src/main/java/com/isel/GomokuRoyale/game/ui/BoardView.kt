package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isel.GomokuRoyale.ui.theme.GomokuRoyaleTheme
import model.Board
import model.Coordinate
import model.Player
import model.openingrule
import model.variantes

@Composable
fun BoardView(
    board: Board,
    onTileSelected: (at: Coordinate) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        repeat(board.boardSize) { row ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(weight = 1.0f, fill = true)
            ) {
                repeat(board.boardSize) { column ->
                    val at = Coordinate(row, column,board.boardSize)
                    PieceView(
                        move = board[at],
                        enabled = enabled,
                        modifier = Modifier.weight(weight = 1.0f, fill = true),
                        onSelected = { onTileSelected(at) },
                    )
                    if (column != board.boardSize - 1) { VerticalSeparator() }
                }
            }
            if (row != board.boardSize - 1) { HorizontalSeparator() }
        }
    }
}

@Composable
private fun HorizontalSeparator() {
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(2.dp)
        .background(MaterialTheme.colorScheme.secondary)
    )
}

@Composable
private fun VerticalSeparator() {
    Spacer(modifier = Modifier
        .fillMaxHeight()
        .width(2.dp)
        .background(MaterialTheme.colorScheme.secondary)
    )
}


@Preview(showBackground = true)
@Composable
private fun EmptyBoardViewPreview() {
    GomokuRoyaleTheme {
        BoardView(board = Board(), enabled = true, onTileSelected = { })
    }
}

@Preview(showBackground = true)
@Composable
private fun NonEmptyBoardViewPreview() {
    GomokuRoyaleTheme {
        BoardView(board = aBoard, enabled = true, onTileSelected = { })
    }
}
private val aVariante = variantes.NORMAL
private val aBoardSize = if (aVariante == variantes.OMOK) 19  else 15
private val aBoard = Board(
    turn = Player.BLACK,
    variantes = aVariante,
    openingrule = openingrule.PRO,
    boardSize = aBoardSize,
    moves = mapOf(
        Coordinate(7,7,aBoardSize) to Player.BLACK,
        Coordinate(1,7,aBoardSize) to Player.WHITE,
        Coordinate(4,7,aBoardSize) to Player.BLACK,
        Coordinate(2,7,aBoardSize) to Player.WHITE)
)

