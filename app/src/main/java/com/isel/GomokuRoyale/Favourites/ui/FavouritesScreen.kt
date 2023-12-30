package com.isel.GomokuRoyale.Favourites.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.isel.GomokuRoyale.R
import com.isel.GomokuRoyale.ui.TopBar
import com.isel.GomokuRoyale.ui.theme.GomokuRoyaleTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.Timestamp
import com.isel.GomokuRoyale.Favourites.GameInfo
import com.isel.GomokuRoyale.game.ui.GameInfoView
import com.isel.GomokuRoyale.ui.NavigationHandlers

const val PlayerFoundViewTag = "PlayerFoundView"
const val GamesViewTag = "RankingsView"
const val LeaderboardScreenTag = "LeaderboardScreenTag"
const val FavouriteScreenTag = "FavouriteScreenTag"

data class FavouritesListState(
    val gameList: List<GameInfo>? = null,
    val error: String? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FavouritesScreen(
    state: FavouritesListState = FavouritesListState(),
    onBackRequest: () -> Unit,
    onErrorReset: () -> Unit,
) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(GamesViewTag),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = { TopBar(NavigationHandlers(onBackRequested = { onBackRequest() })) },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                if (state.gameList != null) {
                    Text(
                        text = stringResource(id = R.string.favourite_games_list),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(innerPadding),
                        userScrollEnabled = true,
                    ) {
                        items(state.gameList.size) {
                            GameInfoView(state.gameList[it], onPlayerSelected = { gameInfo ->  })
                        }
                    }
                }else{
                    Text(
                        text = stringResource(id = R.string.favourite_games_list),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                    Text(
                        text = stringResource(id = R.string.utils_loading),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                }
                if (state.error != null) {
                    Toast.makeText(LocalContext.current, state.error, Toast.LENGTH_LONG).show()
                    onErrorReset()
                }
            }
        }
    }
}
private const val MAX_INPUT_SIZE = 32
private fun ensureInputBounds(input: String) =
    input.also {
        it.substring(range = 0 until Integer.min(it.length, MAX_INPUT_SIZE))
    }

@Preview(showBackground = true)
@Composable
private fun FavouritesWithoutGamesPreview() {
    FavouritesScreen(
        state = FavouritesListState(gameList = emptyList()),
        onBackRequest = {},
        onErrorReset = {}
    )
}