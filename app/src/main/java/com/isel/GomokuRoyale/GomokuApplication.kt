

package com.isel.GomokuRoyale

import android.app.Application
import com.google.gson.GsonBuilder
import com.isel.GomokuRoyale.lobby.domain.Lobby
//import com.isel.GomokuRoyale.login.UserInfoSharedPrefs
import com.isel.GomokuRoyale.preferences.model.LoginService
import com.isel.GomokuRoyale.preferences.model.UserInfoRepository
import okhttp3.OkHttpClient
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.LocalCacheSettings
import com.google.firebase.firestore.ktx.*
import com.google.firebase.ktx.Firebase
import com.isel.GomokuRoyale.game.adapters.MatchFirebase
import com.isel.GomokuRoyale.leaderboard.model.FakeLeaderboardService
import com.isel.GomokuRoyale.leaderboard.model.LeaderboardService
import com.isel.GomokuRoyale.lobby.adapters.LobbyFirebase
import com.isel.GomokuRoyale.preferences.UserInfoSharedPrefs
import com.isel.GomokuRoyale.preferences.model.RealLoginService
import model.Match

const val TAG = "GomokuApp"

interface DependenciesContainer {

    val loginService : LoginService
    val leaderboardService: LeaderboardService
    val userInfoRepo : UserInfoRepository
    val lobby: Lobby
    val match: Match

}


class GomokuApplication:Application(), DependenciesContainer{

    private val emulatedFirestoreDb: FirebaseFirestore by lazy {
        Firebase.firestore.also {
            it.useEmulator("10.0.0.2", 8080)
            it.firestoreSettings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false)
                .build()
        }
    }

    private val realFirestoreDb: FirebaseFirestore by lazy {
        //Firebase.firestore
        emulatedFirestoreDb
    }

    override val match: Match
        get() = MatchFirebase(realFirestoreDb)

    override val lobby: Lobby
        get() = LobbyFirebase(realFirestoreDb)

    override val loginService: LoginService
        get() = RealLoginService(OkHttpClient(), GsonBuilder().create())

    override val leaderboardService: LeaderboardService
        get() = FakeLeaderboardService()

    override val userInfoRepo: UserInfoRepository
        get() = UserInfoSharedPrefs(this)


}

