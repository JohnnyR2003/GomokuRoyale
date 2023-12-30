package com.isel.GomokuRoyale.Favourites.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.isel.GomokuRoyale.DependenciesContainer
import com.isel.GomokuRoyale.Favourites.model.FavouritesViewModel
import com.isel.GomokuRoyale.utils.viewModelInit
import kotlinx.coroutines.launch

class FavouritesListActivity : ComponentActivity(){

    private val viewModel by viewModels<FavouritesViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer).favourites
            FavouritesViewModel(app)
        }
    }

    companion object{
        fun navigateTo(origin: Activity){
            with(origin){
                val intent = Intent(this, FavouritesListActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed(){
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent{
            val error by viewModel.rError.collectAsState()
            val games by viewModel.favourites.collectAsState()
            FavouritesScreen(
                state = FavouritesListState(
                    games,
                    error,
                ),
                onNavigateToGame = {
                    game -> viewModel.selectReplay(game)
                                   },
                onBackRequest = { finish() },
                onErrorReset = {viewModel.resetError()},
            )
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.enterLobby()
                try {
                   viewModel.favourites.collect{
                       viewModel.fetchFavourites()
                       viewModel.selectedGame.collect{
                           if(it != null){
                               FavouritesActivity.navigateTo(
                                   this@FavouritesListActivity,
                                   it.game
                                   )
                           }
                       }
                   }
                }
                finally {
                    viewModel.leaveFavourites()
                    finish()
                }
            }
        }
    }
}