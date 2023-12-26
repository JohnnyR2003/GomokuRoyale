package com.isel.GomokuRoyale.home.ui


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.isel.GomokuRoyale.DependenciesContainer
import com.isel.GomokuRoyale.about.ui.AboutActivity
import com.isel.GomokuRoyale.game.Loaded
import com.isel.GomokuRoyale.game.getOrNull
import com.isel.GomokuRoyale.game.idle
import com.isel.GomokuRoyale.home.model.HomeViewModel
import com.isel.GomokuRoyale.leaderboard.ui.LeaderboardActivity
import com.isel.GomokuRoyale.lobby.ui.LobbyActivity
import com.isel.GomokuRoyale.preferences.model.PreferencesActivity
import com.isel.GomokuRoyale.preferences.model.UserInfo
import com.isel.GomokuRoyale.ui.ErrorAlert
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.isel.GomokuRoyale.R


class HomeActivity() : ComponentActivity() {

    companion object {
        fun navigateTo(origin: ComponentActivity) {
            val intent = Intent(origin, HomeActivity::class.java)
            origin.startActivity(intent)
        }
    }
/*
    private val repo by lazy {
        (application as DependenciesContainer).userInfoRepo
    }
*/
private val vm by viewModels<HomeViewModel> {
    HomeViewModel.factory((application as DependenciesContainer).userInfoRepo)
}

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSplashScreen()

        lifecycleScope.launch {
            vm.userInfo.collectLatest {
                if (it is Loaded && it.value.isSuccess) {
                    doNavigation(userInfo = it.getOrNull())
                    vm.resetToIdle()
                }
            }
        }

        setContent {
            val userInfo by vm.userInfo.collectAsState(initial = idle())
            val loggedState by vm.isLoggedIn.collectAsState()
            HomeView(
                state = HomeScreenState(null, loggedState),
                onLogoutRequest = {
                     vm.logout()
                },
                /*  onMeRequest = { onBackPressed() }   {
                      if (loggedState)
                          MeActivity.navigateTo(this)
                      else
                          LoginActivity.navigateTo(this)
                  ,*/
                onFindGameRequest = {
                    if (loggedState)
                        LobbyActivity.navigateTo(this)
                    else
                        PreferencesActivity.navigateTo(this)
                },
                onLeaderboardRequest = { LeaderboardActivity.navigateTo(this) },
                onInfoRequest = { AboutActivity.navigateTo(this) },
               /* onSignInOrSignUpRequest = {
                    if (!loggedState)
                        LoginActivity.navigateTo(this)
                },*/
                onExitRequest = { finishAndRemoveTask() }
            )
            userInfo.let {
                if (it is Loaded && it.value.isFailure)
                    ErrorAlert(
                        title = R.string.failed_to_read_preferences_error_dialog_title,
                        message =com.isel.GomokuRoyale.R.string.failed_to_read_preferences_error_dialog_text,
                        buttonText = R.string.failed_to_read_preferences_error_dialog_ok_button,
                        onDismiss = { vm.resetToIdle() }
                    )
            }
        }
    }
    private fun doNavigation(userInfo: UserInfo?) {
        if (userInfo == null)
            PreferencesActivity.navigateTo(this)
        else
            LobbyActivity.navigateTo(this)
    }

    private fun setupSplashScreen() {
        var keepSplashScreenOn = true
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                delay(2000)
                keepSplashScreenOn = false
            }
        }

        installSplashScreen().setKeepOnScreenCondition {
            keepSplashScreenOn
        }
    }

}