package com.isel.GomokuRoyale.preferences.model

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.isel.GomokuRoyale.DependenciesContainer
import com.isel.GomokuRoyale.R
import com.isel.GomokuRoyale.game.Saved
import com.isel.GomokuRoyale.game.getOrNull
import com.isel.GomokuRoyale.game.idle
import com.isel.GomokuRoyale.game.ui.GameActivity
import com.isel.GomokuRoyale.preferences.ui.PreferencesScreen
import com.isel.GomokuRoyale.ui.ErrorAlert
import com.isel.GomokuRoyale.utils.viewModelInit
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch
import model.Player

const val FINISH_ON_SAVE_EXTRA = "FinishOnSaveExtra"

/**
 * The screen used to display and edit the user information to be used to identify
 * the player in the lobby.
 */


class PreferencesActivity : ComponentActivity() {

    companion object {
        /**
         * Navigates to the [UserPreferencesActivity] activity.
         * @param ctx the context to be used.
         */
        fun navigateTo(ctx: Context, userInfo: UserInfo? = null) {
            ctx.startActivity(createIntent(ctx, userInfo))
        }

        /**
         * Builds the intent that navigates to the [UserPreferencesActivity] activity.
         * @param ctx the context to be used.
         */
        fun createIntent(ctx: Context, userInfo: UserInfo? = null): Intent {
            val intent = Intent(ctx, PreferencesActivity::class.java)
            userInfo?.let { intent.putExtra(USER_INFO_EXTRA, UserInfoExtra(it)) }
            return intent
        }
    }

    private val vm by viewModels<UserPreferencesScreenViewModel> {
        viewModelInit { UserPreferencesScreenViewModel((application as DependenciesContainer).userInfoRepo) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            vm.ioState.collect {
                if (it is Saved && it.value.isSuccess)
                    finish()
            }

        }

        setContent {
            val ioState by vm.ioState.collectAsState(initial = idle())
            PreferencesScreen(
                userInfo = ioState.getOrNull() ?: userInfoExtra,
                onSaveRequested = { userInfoToSave ->
                    run {
                        vm.updateUserInfo(userInfoToSave)
                        GameActivity.navigate(
                            this,
                            Player.BLACK,
                            userInfoToSave.variante,
                            userInfoToSave.openingrule,
                            userInfoToSave.title
                        )
                    }
                },
                onBackRequested = { finish() }
            )

            ioState.let {
                if (it is Saved && it.value.isFailure)
                    ErrorAlert(
                        title = R.string.failed_to_save_preferences_error_dialog_title,
                        message = R.string.failed_to_save_preferences_error_dialog_text,
                        buttonText = R.string.failed_to_save_preferences_error_dialog_retry_button,
                        onDismiss = { vm.resetToIdle() }
                    )
            }
        }
    }

    /**
     * Helper method to get the user info extra from the intent.
     */
    private val userInfoExtra: UserInfo? by lazy { getUserInfoExtra()?.toUserInfo() }

    @Suppress("DEPRECATION")
    private fun getUserInfoExtra(): UserInfoExtra? =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
            intent?.getParcelableExtra(USER_INFO_EXTRA, UserInfoExtra::class.java)
        else
            intent?.getParcelableExtra(USER_INFO_EXTRA)
}

private const val USER_INFO_EXTRA = "UserInfo"

/**
 * Represents the data to be passed as an extra in the intent that navigates to the
 * [UserPreferencesActivity]. We use this class because the [UserInfo] class is not
 * parcelable and we do not want to make it parcelable because it's a domain class.
 */
@Parcelize
private data class UserInfoExtra(val variante: String, val openingrule: String, val title: String) : Parcelable {
    constructor(userInfo: UserInfo) : this(userInfo.variante, userInfo.openingrule, userInfo.title)
}

private fun UserInfoExtra.toUserInfo() = UserInfo(variante, openingrule,title)