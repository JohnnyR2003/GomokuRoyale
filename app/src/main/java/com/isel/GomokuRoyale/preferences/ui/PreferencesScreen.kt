package com.isel.GomokuRoyale.preferences.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isel.GomokuRoyale.R
import com.isel.GomokuRoyale.preferences.model.UserInfo
import com.isel.GomokuRoyale.preferences.model.userInfoOrNull
import com.isel.GomokuRoyale.ui.EditFab
import com.isel.GomokuRoyale.ui.FabMode
import com.isel.GomokuRoyale.ui.IsReadOnly
import com.isel.GomokuRoyale.ui.NavigationHandlers
import com.isel.GomokuRoyale.ui.TopBar
import com.isel.GomokuRoyale.ui.theme.GomokuRoyaleTheme
import kotlin.math.min

const val PreferencesScreenTag = "PreferencesScreen"
const val NicknameInputTag = "NicknameInput"
const val MotoInputTag = "MotoInput"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(
    userInfo: UserInfo?,
    onBackRequested: () -> Unit = { },
    onSaveRequested: (UserInfo) -> Unit = { }
) {
    GomokuRoyaleTheme {

        var displayedNick by remember { mutableStateOf(userInfo?.username ?: "") }
        var displayedStatus by remember { mutableStateOf(userInfo?.status ?: "") }
        var editing by remember { mutableStateOf(userInfo == null) }

        val enteredInfo = userInfoOrNull(
            username = displayedNick.trim(),
            status = displayedStatus.trim()
        )

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(PreferencesScreenTag),
            topBar = { TopBar(NavigationHandlers(onBackRequested)) },
            floatingActionButton = {
                EditFab(
                    onClick =
                    if (!editing) { { editing = true } }
                    else if (enteredInfo == null) null
                    else { { onSaveRequested(enteredInfo) } },
                    mode = if (editing) FabMode.Save else FabMode.Edit
                )
            }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Text(
                    text = stringResource(id = R.string.preferences_screen_title),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(50.dp))

                OutlinedTextField(
                    value = displayedNick,
                    onValueChange = { displayedNick = ensureInputBounds(it) },
                    singleLine = true,
                    label = {
                        Text(stringResource(id = R.string.preferences_screen_nickname_tip))
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Face, contentDescription = "")
                    },
                    readOnly = !editing,
                    modifier = Modifier
                        .padding(horizontal = 48.dp)
                        .fillMaxWidth()
                        .testTag(NicknameInputTag)
                        .semantics { if (!editing) this[IsReadOnly] = Unit }
                )
                OutlinedTextField(
                    value = displayedStatus,
                    onValueChange = { displayedStatus = ensureInputBounds(it) },
                    maxLines = 3,
                    label = { Text(stringResource(id = R.string.preferences_screen_status_tip)) },
                    leadingIcon = {
                        Icon(Icons.Default.Comment, contentDescription = "")
                    },
                    readOnly = !editing,
                    modifier = Modifier
                        .padding(horizontal = 48.dp)
                        .fillMaxWidth()
                        .testTag(MotoInputTag)
                        .semantics { if (!editing) this[IsReadOnly] = Unit }
                )
                Spacer(
                    modifier = Modifier
                        .sizeIn(minHeight = 128.dp, maxHeight = 256.dp)
                )
            }
        }
    }
}

private const val MAX_INPUT_SIZE = 50
private fun ensureInputBounds(input: String) =
    input.also {
        it.substring(range = 0 until min(it.length, MAX_INPUT_SIZE))
    }

@Preview(showBackground = true)
@Composable
private fun PreferencesScreenViewModePreview() {
    PreferencesScreen(
        userInfo = UserInfo("my nick", "my moto"),
        onSaveRequested = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun PreferencesScreenEditModePreview() {
    PreferencesScreen(
        userInfo = null,
        onSaveRequested = { }
    )
}