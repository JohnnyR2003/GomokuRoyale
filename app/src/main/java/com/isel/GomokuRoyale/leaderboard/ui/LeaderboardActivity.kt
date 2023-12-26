package com.isel.GomokuRoyale.leaderboard.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.isel.GomokuRoyale.DependenciesContainer
import com.isel.GomokuRoyale.leaderboard.model.LeaderboardViewModel
import com.isel.GomokuRoyale.utils.viewModelInit

class LeaderboardActivity : ComponentActivity(){

    private val viewModel by viewModels<LeaderboardViewModel> {
        viewModelInit {
            LeaderboardViewModel()
        }
    }

    companion object{
        fun navigateTo(origin: Activity){
            with(origin){
                val intent = Intent(this, LeaderboardActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed(){
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        viewModel.fetchRankings(10)
        setContent{
            val error by viewModel.rError.collectAsState()
            val playerFound by viewModel.rplayerFound.collectAsState()
            val rankings by viewModel.rRankings.collectAsState()
            LeaderboardView(
                state = LeaderboardState(
                    playerFound,
                    rankings,
                    error,
                ),
                onFindPlayer = {username -> viewModel.fetchPlayerInfo(username) },
                onBackRequest = { finish() },
                onErrorReset = {viewModel.resetError()},
            )
        }
    }
}