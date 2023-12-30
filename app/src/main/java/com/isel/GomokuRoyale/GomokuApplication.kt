

package com.isel.GomokuRoyale

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

//import com.isel.GomokuRoyale.login.UserInfoSharedPrefs
import com.isel.GomokuRoyale.preferences.model.UserInfoRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.*
import com.google.firebase.ktx.Firebase
import com.isel.GomokuRoyale.Favourites.Fav
import com.isel.GomokuRoyale.Favourites.adapters.FavFirebase
import com.isel.GomokuRoyale.game.adapters.MatchFirebase
import com.isel.GomokuRoyale.preferences.UserInfoDataStore
import model.Match

const val TAG = "GomokuApp"

interface DependenciesContainer {

    //val loginService : LoginService
    //val leaderboardService: LeaderboardService
    val userInfoRepo : UserInfoRepository
    val match: Match
    val favourites : Fav

}


class GomokuApplication:Application(), DependenciesContainer{

    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_info")

    private val emulatedFirestoreDb: FirebaseFirestore by lazy {
        Firebase.firestore.also {
            it.useEmulator("10.0.2.2", 8080)
            it.firestoreSettings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false)
                .build()
        }
    }

    private val realFirestoreDb: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    override val match: Match
        get() = MatchFirebase(emulatedFirestoreDb)


    override val favourites: Fav
        get() = FavFirebase(emulatedFirestoreDb)
    /*
    override val loginService: LoginService
        get() = RealLoginService(OkHttpClient(), GsonBuilder().create())

    override val leaderboardService: LeaderboardService
        get() = FakeLeaderboardService()
*/
    override val userInfoRepo: UserInfoRepository
        get() = UserInfoDataStore(dataStore)


}

