package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isel.GomokuRoyale.R
import com.isel.GomokuRoyale.ui.theme.GomokuRoyaleTheme
import model.Player

internal const val TileViewTag = "TileView"

@Composable
fun PieceView(
    move: Player?,
    enabled: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxSize()
        .padding(1.dp)
        .testTag(TileViewTag)
        .clickable(enabled = move == null && enabled) { onSelected() }
    ) {
        if (move != null) {
            val marker = when (move) {
                Player.WHITE-> R.drawable.whitepiece
                Player.BLACK -> R.drawable.blackpiece
            }
            Image(
                painter = painterResource(id = marker),
                contentDescription = "",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TileViewCirclePreview() {
    GomokuRoyaleTheme {
        PieceView(move = Player.WHITE, enabled = true, onSelected = { })
    }
}


@Preview(showBackground = true)
@Composable
private fun TileViewCrossPreview() {
    GomokuRoyaleTheme {
        PieceView(move = Player.BLACK, enabled = true, onSelected = { })
    }
}

@Preview(showBackground = true)
@Composable
private fun TileViewEmptyPreview() {
    GomokuRoyaleTheme {
        PieceView(move = null, enabled = true, onSelected = { })
    }
}
