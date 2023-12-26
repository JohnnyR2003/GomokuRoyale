import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isel.GomokuRoyale.R
import com.isel.GomokuRoyale.lobby.domain.PlayerInfo
import com.isel.GomokuRoyale.preferences.model.UserInfo
import com.isel.GomokuRoyale.ui.NavigationHandlers
import com.isel.GomokuRoyale.ui.TopBar
import com.isel.GomokuRoyale.ui.theme.GomokuRoyaleTheme

const val LobbyScreenTag = "LobbyScreen"
const val PlayerInfoViewTag = "PlayerInfoView"

data class LobbyState(
    val players: List<PlayerInfo> = emptyList()
)

@Composable
fun PlayerInfoView(
    playerInfo: PlayerInfo,
    onPlayerSelected: (PlayerInfo) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlayerSelected(playerInfo) }
            .testTag(PlayerInfoViewTag)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = playerInfo.info.username,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            if (playerInfo.info.status != null) {
                Text(
                    text = playerInfo.info.status!!,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, end = 8.dp),
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen(
    state: LobbyState = LobbyState(),
    onPlayerSelected: (PlayerInfo) -> Unit = { },
    onBackRequested: () -> Unit = { },
    onPreferencesRequested: () -> Unit = { }
) {

    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(LobbyScreenTag),
            topBar = {
                TopBar(
                    NavigationHandlers(
                        onBackRequested = onBackRequested,
                        onChallengeRequested = onPreferencesRequested,
                        ),
                )
            },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.lobby_lobby),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(innerPadding)
                ) {
                    items(state.players) {
                        PlayerInfoView(playerInfo = it, onPlayerSelected)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserInfoViewNoMotoPreview() {
    GomokuRoyaleTheme {
        PlayerInfoView(
            playerInfo = PlayerInfo(UserInfo("My Nick", "")),
            onPlayerSelected = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserInfoViewPreview() {
    GomokuRoyaleTheme {
        PlayerInfoView(
            playerInfo = PlayerInfo(UserInfo("My Nick", "This is my moto")),
            onPlayerSelected = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LobbyScreenPreview() {
    LobbyScreen(
        state = LobbyState(players),
        onBackRequested = { },
        onPreferencesRequested = { }
    )
}

private val players = buildList {
    repeat(30) {
        add(PlayerInfo(UserInfo("My Nick $it", "This is my $it moto")))
    }
}