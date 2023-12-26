package com.isel.GomokuRoyale.lobby.model

import com.google.gson.Gson
import com.isel.GomokuRoyale.preferences.model.UserInfo
import model.Lounge
import okhttp3.OkHttpClient
import okhttp3.Request

interface LobbyService {

    suspend fun getRooms(): List<Lounge>

    suspend fun joinMatch(gameId: String): String

    suspend fun createMatch(spr: Int): String

}

class FakeLobbyService(private val userInfo: UserInfo): LobbyService{

    var rooms :List<Lounge> = listOf(
        Lounge("fake", userInfo.username, 1),
        Lounge("fake2", userInfo.username, 1),
        Lounge("fake3", userInfo.username, 1),
        Lounge("fake4", userInfo.username, 1),
        Lounge("fake5", userInfo.username, 1),
        Lounge("fake6", userInfo.username, 1),
    )

    override suspend fun getRooms(): List<Lounge> {
        return rooms
    }

    override suspend fun joinMatch(gameId: String): String {
        val match = rooms.filter { it.uuid == gameId }[0].uuid
        if (match == null) return "No match found"
        else return "Joined match $match"
    }

    override suspend fun createMatch(variant: Int): String {
       rooms = rooms + Lounge("test", userInfo.username, variant)
        return "Match created successfully"
    }

}
class RealLobbyService(
    private val userInfo: UserInfo,
    private val client: OkHttpClient,
    private val gson: Gson
): LobbyService {

    private val request: Request by lazy {
        Request.Builder()
            .url("https://battleship.isel.pt/game/")
            .addHeader("accept", "application/json")
            .build()
    }

    override suspend fun getRooms(): List<Lounge> {
        client.newCall(request).execute().use { response ->
            val body = response.body?.string()
            val rooms = gson.fromJson(body, Array<Lounge>::class.java)
            return rooms.filter { it.player1 != userInfo.username }
        }
    }

    override suspend fun joinMatch(gameId: String): String {
        return ""
    }

    override suspend fun createMatch(variant: Int): String {
        return ""
    }

}