package com.isel.GomokuRoyale.leaderboard.model

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.lang.reflect.Type

interface LeaderboardService {

    suspend fun getPlayerInfo(name: String): PlayerInfo

    suspend fun rankings(pages: Int): List<PlayerInfo>
}

class FakeLeaderboardService : LeaderboardService {
    override suspend fun getPlayerInfo(name: String): PlayerInfo {
        val player = ranking.filter { it.username == name }
        return player[0]
    }

    override suspend fun rankings(pages: Int): List<PlayerInfo> {
        return ranking
    }

  val ranking = listOf(
            PlayerInfo("name1", 1, 6),
            PlayerInfo("name2", 2, 5),
            PlayerInfo("name3", 3, 4),
            PlayerInfo("name4", 4, 3),
            PlayerInfo("name5", 5, 2),
            PlayerInfo("name6", 6, 1)
        )
    }


/*
class RealLeaderboardService(
    private val httpClient: OkHttpClient,
    private val jsonEncoder: Gson
) : LeaderboardService {
    override suspend fun getPlayerInfo(name: String): PlayerInfo {
        val request = Request.Builder()
            .url("$HOST/players/player?username=$name")
            .get()
            .build()
        return httpClient.newCall(request).execute().use { response ->
            handleResponse(response, PlayerInfo::class.java)
        }
    }

    override suspend fun rankings(pages: Int): List<PlayerInfo> {
        val request = Request.Builder()
            .url("$HOST/players/rankings?pages=$pages")
            .get()
            .build()
        httpClient.newCall(request).execute().use { response ->
            return handleResponse<Array<PlayerInfo>>(response,Array<PlayerInfo>::class.java).map{it}
        }
    }

    /**
     * This method's usefulness is circumstantial. In more realistic scenarios
     * we will not be handling API responses with this simplistic approach.
     */
    private fun <T> handleResponse(response: Response, type: Type): T {
        val contentType = response.body?.contentType()
        return if (response.isSuccessful && contentType != null && contentType == ApplicationJsonType) {
            try {
                val body = response.body?.string()
                jsonEncoder.fromJson<T>(body, type)
            } catch (e: JsonSyntaxException) {
                throw UnexpectedResponseException(response)
            }
        } else {
            val body = response.body?.string()
            throw ResponseException(body.orEmpty())
        }
    }

    abstract class ApiException(msg: String) : Exception(msg)

    /**
     * Exception throw when an unexpected response was received from the API.
     */
    class UnexpectedResponseException(
        val response: Response? = null
    ) : ApiException("Unexpected ${response?.code} response from the API.")

    class ResponseException(
        response: String
    ) : ApiException(response)


}
*/
