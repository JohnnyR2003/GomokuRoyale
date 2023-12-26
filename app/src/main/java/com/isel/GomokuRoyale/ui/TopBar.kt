package com.isel.GomokuRoyale.ui

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isel.GomokuRoyale.R
import com.isel.GomokuRoyale.ui.theme.GomokuRoyaleTheme


data class NavigationHandlers (
    val onBackRequested: (() -> Unit)? = null,
    val onInfoRequested: (() -> Unit)? = null,
    val onLogoutRequested: (() -> Unit)? = null,
    val onRefreshRequested: (() -> Unit)? = null,
    val onChallengeRequested: (() -> Unit)? = null,
    //val onLeaveRequested: (() -> Unit)? = null,
    //val onInspectRequested: (() -> Unit)? = null,
)

// Test tags for the TopBar navigation elements
const val NavigateBackTestTag = "NavigateBack"
const val NavigateToInfoTestTag = "NavigateToInfo"

val cornerRadius = 16.dp
val gradientColor = listOf(Color(0xFFff00cc), Color(0xFF333399))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navigation: NavigationHandlers = NavigationHandlers(),text:String = "Gomoku Royale") {
    TopAppBar(
        modifier = Modifier.testTag("TopBar"),
        /*colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF9452A5),
        ),*/
        title = { Text(text = text) },
        navigationIcon = {
            if (navigation.onBackRequested != null) {
                IconButton(
                    onClick = navigation.onBackRequested,
                    modifier = Modifier.testTag(NavigateBackTestTag)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.top_bar_go_back)
                    )
                }
            }
        },
        actions = {
            if(navigation.onChallengeRequested != null){
                IconButton(
                    onClick = navigation.onChallengeRequested,
                    modifier = Modifier.testTag(NavigateBackTestTag)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.top_bar_add_challenge)
                    )
                }
            }
            if (navigation.onInfoRequested != null) {
                IconButton(
                    onClick = navigation.onInfoRequested,
                    modifier = Modifier.testTag(NavigateToInfoTestTag)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = stringResource(id = R.string.top_bar_navigate_to_about)
                    )
                }
            }
        }
    )}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradientTopBar(
    navigation: NavigationHandlers = NavigationHandlers(),
    text:String = "Gomoku Royale"
) {
    TopAppBar(
        title = { Text(text = text) },
        navigationIcon = {
            if (navigation.onBackRequested != null) {
                IconButton(
                    onClick = navigation.onBackRequested,
                    modifier = Modifier.testTag(NavigateBackTestTag)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.top_bar_go_back)
                    )
                }
            }
        },
        actions = {
            if (navigation.onInfoRequested != null) {
                IconButton(
                    onClick = navigation.onInfoRequested,
                    modifier = Modifier.testTag(NavigateToInfoTestTag)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = stringResource(id = R.string.top_bar_navigate_to_about)
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun TopBarPreviewInfoAndHistory() {
    TopBar(
        navigation = NavigationHandlers(onInfoRequested = { })
    )
}

@Preview
@Composable
private fun TopBarPreviewBackAndInfo() {
    GomokuRoyaleTheme {
        TopBar(navigation = NavigationHandlers(onBackRequested = { }, onInfoRequested = { }))
    }
}

@Preview
@Composable
private fun TopBarPreviewBack() {
    GomokuRoyaleTheme {
        TopBar(navigation = NavigationHandlers(onBackRequested = { }))
    }
}