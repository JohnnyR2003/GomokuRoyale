/*
package com.isel.GomokuRoyale

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.isel.GomokuRoyale.about.ui.AboutActivity
import com.isel.GomokuRoyale.home.model.HomeViewModel
import com.isel.GomokuRoyale.home.ui.HomeActivity
import com.isel.GomokuRoyale.home.ui.HomeScreenState
import com.isel.GomokuRoyale.home.ui.HomeView
import com.isel.GomokuRoyale.leaderboard.model.LeaderboardViewModel
import com.isel.GomokuRoyale.leaderboard.model.PlayerInfo
import com.isel.GomokuRoyale.leaderboard.ui.LeaderboardActivity
import com.isel.GomokuRoyale.leaderboard.ui.LeaderboardScreenTag
import com.isel.GomokuRoyale.leaderboard.ui.LeaderboardState
import com.isel.GomokuRoyale.leaderboard.ui.LeaderboardView
//import com.isel.GomokuRoyale.login.UserInfoSharedPrefs
import com.isel.GomokuRoyale.login.model.FakeLoginService
import com.isel.GomokuRoyale.login.model.LoginActivity
import com.isel.GomokuRoyale.login.model.LoginService
import com.isel.GomokuRoyale.login.model.UserInfo
import com.isel.GomokuRoyale.login.model.UserInfoDataStore
import com.isel.GomokuRoyale.login.model.UserInfoRepository
import com.isel.GomokuRoyale.ui.theme.GomokuRoyaleTheme
import kotlinx.coroutines.launch


val leaderboardvm = LeaderboardViewModel()

class MainActivity : ComponentActivity() {
    private val repo by lazy {
        (application as DependenciesContainer).userInfoRepo
    }
    private val vm by viewModels<MainScreenViewModel> {
        MainScreenViewModel.factory((application as DependenciesContainer).userInfoRepo)
    }
    //val homevm: HomeViewModel
      //  get() = HomeViewModel(repo)
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            if (repo.getUserInfo() == null)
                LoginActivity.navigateTo(this@MainActivity)
            else
                HomeActivity.navigateTo(this@MainActivity)
        }

            setContent {
                val playerfound = leaderboardvm.rplayerFound.collectAsState()
                GomokuRoyaleTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                      val scope = rememberCoroutineScope()
                        HomeView(
                            state = HomeScreenState(null, false),
                            onLogoutRequest = { LoginActivity.navigateTo(this) },
                            onMeRequest = {
                              */
/*  if (false)
                                    MeActivity.navigateTo(this)
                                else
                                    LoginActivity.navigateTo(this)*//*

                            },
                            onFindGameRequest = {
                               // if (false)
                                    //LobbyActivity.navigateTo(this)
                                //else
                                    LoginActivity.navigateTo(this)
                            },
                            onLeaderboardRequest = {
                                if (false)
                                    LeaderboardActivity.navigateTo(this)
                                else
                                    LoginActivity.navigateTo(this)
                            },
                            onInfoRequest = {

                                    AboutActivity.navigateTo(this)
                            },
                            onSignInOrSignUpRequest = {

                                    LoginActivity.navigateTo(this)
                            },
                            onExitRequest = {
                                finish()
                            }
                        )
                    }


                        }
                    }
                }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GomokuRoyaleTheme {
        Greeting("Android")
    }
}*/
