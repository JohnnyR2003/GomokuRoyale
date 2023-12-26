package com.isel.GomokuRoyale.home.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isel.GomokuRoyale.R
import com.isel.GomokuRoyale.preferences.model.UserInfo
import com.isel.GomokuRoyale.ui.GradientButton
//import com.isel.GomokuRoyale.login.model.Token
//import com.isel.GomokuRoyale.login.model.UserInfo
//import com.example.finalprojectv2.ui.LoggedState
import com.isel.GomokuRoyale.ui.NavigationHandlers
//import com.isel.GomokuRoyale.ui.RefreshingState
import com.isel.GomokuRoyale.ui.TopBar
import com.isel.GomokuRoyale.ui.theme.GomokuRoyaleTheme

data class HomeScreenState(
    val user: UserInfo? = null,
    val loggedState: Boolean = false
)

const val HomeScreenTag = "HomeScreen"

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeView(
    state: HomeScreenState = HomeScreenState(),
    onLogoutRequest: () -> Unit,
    //onMeRequest: () -> Unit,
    onFindGameRequest: () -> Unit,
    onLeaderboardRequest: () -> Unit,
    onInfoRequest: () -> Unit,
   // onSignInOrSignUpRequest: () -> Unit,
    onExitRequest: () -> Unit
) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(HomeScreenTag),
            topBar = {
                if (state.loggedState) {
                    TopBar(
                        NavigationHandlers(
                            onLogoutRequested = { onLogoutRequest() },
                            onInfoRequested = { onInfoRequest() }),
                        ""
                    )
                } else {
                    TopBar(
                        NavigationHandlers(onInfoRequested = { onInfoRequest() }) , ""
                    )
                }
            }
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Row() {
                    Text(
                        text = "Gomoku Royale",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                ButtonView(
                    onClickRequest = { onFindGameRequest() },
                    name = stringResource(id = R.string.home_play_button),
                    state = true,
                    testTag = "PlayButton"
                )/*
                ButtonView(
                    onClickRequest = { onMeRequest() },
                    name = stringResource(id = R.string.home_profile_button),
                    state = state.loggedState,
                    testTag = "ProfileButton"

                )*/
                ButtonView(onClickRequest = { onLeaderboardRequest() },
                    name = stringResource(id = R.string.home_leaderboard_button),
                    testTag = "LeaderboardButton"
                )/*
                if (!state.loggedState) {
                    ButtonView(
                        onClickRequest = { onSignInOrSignUpRequest() },
                        name = stringResource(id = R.string.home_signinup_button),
                        testTag = "SignInAndSignUpButton"
                    )
                }*/
                ButtonView(
                    onClickRequest = { onExitRequest() },
                    name = stringResource(id = R.string.home_exit_button),
                    testTag = "ExitButton"
                )
            }
        }
        }
}

@Composable
fun ButtonView(onClickRequest: () -> Unit, name: String, state: Boolean? = null, testTag: String) {
    if (state == null) {
        Row() {
            GradientButton(
                gradientColors = listOf(Color(0xFF79F1A4), Color(0xFF0E5CAD)),
                cornerRadius = 16.dp,
                nameButton = name,
                roundedCornerShape = RoundedCornerShape(16.dp),
                onClick = { onClickRequest() },
                modifier = Modifier.testTag(testTag),
                enabled = true
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineLarge,
                )
            }
        }
    } else {
        Row() {
            GradientButton(
                gradientColors = listOf(Color(0xFF79F1A4), Color(0xFF0E5CAD)),
                cornerRadius = 16.dp,
                nameButton = name,
                roundedCornerShape = RoundedCornerShape(16.dp),
                onClick = { onClickRequest() },
                modifier = Modifier.testTag(testTag),
                enabled = state
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineLarge,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun HomePreviewLoggedIn() {
    HomeView(
        state = HomeScreenState(UserInfo("Teste", "Teste"), true),
        onLogoutRequest = {},
       // onMeRequest = {},
        onFindGameRequest = {},
        onLeaderboardRequest = {},
        onInfoRequest = {},
       // onSignInOrSignUpRequest = {},
        onExitRequest = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun HomePreviewLoggedOut() {
    GomokuRoyaleTheme {
        HomeView(
            state = HomeScreenState(null, false),
            onLogoutRequest = {},
           // onMeRequest = {},
            onFindGameRequest = {},
            onLeaderboardRequest = {},
            onInfoRequest = {},
           // onSignInOrSignUpRequest = {},
            onExitRequest = {}
        )
    }
}