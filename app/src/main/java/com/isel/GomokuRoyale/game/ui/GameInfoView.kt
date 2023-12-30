package com.isel.GomokuRoyale.game.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.isel.GomokuRoyale.Favourites.GameInfo


const val PlayerInfoViewTag = "PlayerInfoView"

@Composable
fun GameInfoView(
    gameInfo: GameInfo,
    onPlayerSelected: (GameInfo) -> Unit = {}
){
    Card(
        shape = MaterialTheme.shapes.medium,
        //elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth().clickable { onPlayerSelected(gameInfo) }
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Column {
                Text(
                    text = "title",
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
                )
                /* */
            }


            Column {
                Text(
                    text = "opponent",
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
                )
                /*
                     */

            }

            Column {
                Text(
                    text = "date",
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
                )
                /**/
            }
            Column {
                Text(
                    text = "time",
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
                )
                /**/
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Column{
                Text(
                    text = gameInfo.title,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
                )
            }
            Column {
                Text(
                    text = gameInfo.opponent,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
                )
            }
            Column {
                Text(
                    text = gameInfo.date.toDate().toString(),
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
                )
            }
            Column {
                Text(
                    text = gameInfo.time.toDate().toString(),
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
                )
            }
        }
    }
}


/*
@Composable
fun GameInfoView(
    gameInfo: GameInfo,
    onPlayerSelected: (GameInfo) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlayerSelected(gameInfo) }
            .testTag(PlayerInfoViewTag)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = gameInfo.time,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Column {
            Text(
                text = gameInfo.opponent,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 8.dp),
            )
        }
        Column {
            Text(
                text = gameInfo.date,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 8.dp),
            )
        }
        Column {
            Text(
                text = gameInfo.time,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 8.dp),
            )
        }

    }
}*/
