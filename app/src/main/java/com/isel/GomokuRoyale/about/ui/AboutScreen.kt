package com.isel.GomokuRoyale.about.ui

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isel.GomokuRoyale.R
import com.isel.GomokuRoyale.about.model.Author
//import com.isel.GomokuRoyale.about.model.Author
import com.isel.GomokuRoyale.ui.NavigationHandlers
import com.isel.GomokuRoyale.ui.TopBar
import com.isel.GomokuRoyale.ui.theme.GomokuRoyaleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutView(
    onBackRequest: () -> Unit,
    onSendEmailRequested: (String) -> Unit = { },
    onUrlRequested: (Uri) -> Unit = { },
    listOfAuthor: List<Author>
) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag("AboutView"),
            topBar = { TopBar(navigation = NavigationHandlers(onBackRequest, null)) }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Row(horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = stringResource(id = R.string.about_gomoku),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                repeat(listOfAuthor.size) {
                    AuthorView(
                        onOpenSendEmailRequested = onSendEmailRequested,
                        onOpenUrlRequested = onUrlRequested,
                        listOfAuthor[it]
                    )
                }
            }
        }
    }
}
@Composable
fun AuthorView(
    onOpenSendEmailRequested: (String) -> Unit = { },
    onOpenUrlRequested: (Uri) -> Unit = { },
    author: Author
) {
    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom) {
        Text(
            text = author.name,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top) {
        Button(colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.inversePrimary),onClick = {
            onOpenSendEmailRequested(author.email)
        }) {
            Image(
                painter = painterResource(id = R.drawable.ic_email),
                contentDescription = null,
                modifier = Modifier.sizeIn(25.dp, 25.dp, 50.dp, 50.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.inversePrimary),onClick = {
            onOpenUrlRequested(Uri.parse(author.github))
        }) {
            Image(
                painter = painterResource(id = R.drawable.github_logo),
                contentDescription = null,
                modifier = Modifier.sizeIn(25.dp, 25.dp, 50.dp, 50.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AboutPreview() {
    val listOfAuthor: List<Author> = listOf(
        Author(
            "João Ramos",
            "A49424@alunos.isel.pt",
            "https://github.com/JohnnyR2003"
        ),
        Author(
            "Danilo Vieira",
            "A49988@alunos.isel.pt",
            "https://github.com/A49988"
        ),
        Author(
            "Ricardo Cristino",
            "A49413@alunos.isel.pt",
            "https://github.com/RicardoCristino")
    )
    AboutView(
        onBackRequest = {},
        onSendEmailRequested = {},
        onUrlRequested = {},
        listOfAuthor
    )
}