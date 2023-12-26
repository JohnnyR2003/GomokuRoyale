package com.isel.GomokuRoyale.leaderboard.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

val list = listOf<PlayerInfo>(
    PlayerInfo("joe",1,1),
    PlayerInfo("Biden",1,0),
    PlayerInfo("Trump",1,0),
    PlayerInfo("Obama",1,0),
    PlayerInfo("Bush",9,11),
    PlayerInfo("Clinton",1,0),
    PlayerInfo("Reagan",1,0),
    PlayerInfo("Carter",1,0),
    PlayerInfo("Ford",1,0),
    PlayerInfo("Nixon",1,0),
    PlayerInfo("Johnson",1,0),
    PlayerInfo("Kennedy",1,0),
    PlayerInfo("Eisenhower",1,0),
    PlayerInfo("Truman",1,0))

class LeaderboardViewModel(val leaderboard:List<PlayerInfo> = list ): ViewModel() {

    private val rankings:MutableStateFlow<List<PlayerInfo>> = MutableStateFlow(leaderboard)
    val rRankings = rankings.asStateFlow()

    private val playerFound:MutableStateFlow<PlayerInfo?> = MutableStateFlow(null)
    val rplayerFound = playerFound.asStateFlow()

    private val error : MutableStateFlow<String?> = MutableStateFlow(null)
    val rError = error.asStateFlow()

    fun fetchRankings(pages : Int) {
        rankings.value = leaderboard
    }

    fun fetchPlayerInfo(name: String) {
        playerFound.value = leaderboard.find {  it.username.contains(name) }
    }

    fun resetError() {
        error.value = null
    }



}