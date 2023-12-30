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
import androidx.lifecycle.viewModelScope
import com.isel.GomokuRoyale.DependenciesContainer
import com.isel.GomokuRoyale.Favourites.model.FavouritesViewModel
import com.isel.GomokuRoyale.game.ui.GameActivity
import com.isel.GomokuRoyale.utils.viewModelInit
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class FavouritesActivity : ComponentActivity(){

    private val viewModel by viewModels<FavouritesViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer).favourites
            FavouritesViewModel(app)
        }
    }

    companion object{
        fun navigateTo(origin: Activity){
            with(origin){
                val intent = Intent(this, FavouritesActivity::class.java)
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
                   }
                }
                finally {
                    viewModel.leaveFavourites()
                }
            }
        }
    }
}