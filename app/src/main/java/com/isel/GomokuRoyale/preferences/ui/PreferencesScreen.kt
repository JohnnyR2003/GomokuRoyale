package com.isel.GomokuRoyale.preferences.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isel.GomokuRoyale.preferences.model.UserInfo
import com.isel.GomokuRoyale.preferences.model.userInfoOrNull
import com.isel.GomokuRoyale.ui.EditFab
import com.isel.GomokuRoyale.ui.FabMode
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

        var variantIsExpanded by remember { mutableStateOf(false) }
        var openingruleIsExpanded by remember { mutableStateOf(false) }
        var option1 by remember { mutableStateOf(userInfo?.variante ?: "") }
        var option2 by remember { mutableStateOf(userInfo?.openingrule ?: "") }
        var editing by remember { mutableStateOf(userInfo == null) }

        val enteredInfo = userInfoOrNull(
            variante = option1.trim(),
            openingrule = option2.trim()
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
                Text(text = "Your game's variant")
                Spacer(modifier = Modifier.height(8.dp))
                ExposedDropdownMenuBox(
                    expanded = variantIsExpanded,
                    onExpandedChange = { variantIsExpanded = it },
                ) {
                    TextField(
                        value = option1,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = variantIsExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.menuAnchor()
                        )

                    ExposedDropdownMenu(
                        expanded = variantIsExpanded,
                        onDismissRequest = { variantIsExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(text = "NORMAL")
                            },
                            onClick = {
                                option1 = "NORMAL"
                                variantIsExpanded = !variantIsExpanded
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = "OMOK")
                            },
                            onClick = {
                                option1 = "OMOK"
                                variantIsExpanded = !variantIsExpanded
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                Text(text = "Your game's opening rule")
                Spacer(modifier = Modifier.height(8.dp))
                ExposedDropdownMenuBox(
                    expanded = openingruleIsExpanded,
                    onExpandedChange = { openingruleIsExpanded = it },
                ) {
                    TextField(
                        value = option2,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = openingruleIsExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.menuAnchor()
                        )
                    ExposedDropdownMenu(
                        expanded = openingruleIsExpanded,
                        onDismissRequest = { openingruleIsExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(text = "PRO")
                            },
                            onClick = {
                                option2 = "PRO"
                                openingruleIsExpanded = !openingruleIsExpanded
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = "LONGPRO")
                            },
                            onClick = {
                                option2 = "LONGPRO"
                                openingruleIsExpanded = !openingruleIsExpanded
                            }
                        )
                    }
                }
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
        userInfo = UserInfo("OMOK", "LONGPRO"),
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