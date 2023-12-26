package com.isel.GomokuRoyale.preferences.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.reflect.Type
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response


val ApplicationJsonType = "application/json".toMediaType()

interface LoginService {

    suspend fun login(username: String, password: String): Token

    suspend fun register(username: String, password: String): Token
}
/*
class FakeLoginService: LoginService{

    val users: List<UserInfo> = listOf(
        UserInfo("user1","token"),
        UserInfo("user2","token"),
        UserInfo("user3","token"),
        UserInfo("user4","pass4"),
        UserInfo("user5","pass5"),
        UserInfo("user6","pass6"),
        UserInfo("user7","pass7")
    )

    override suspend fun login(username: String, password: String): Token {
        if (users.filter{ it.username == username }.size != 0 && users.filter { it.bearer == password }.size != 0) {
            return Token("token")
        }
        else
            throw Exception("Invalid username or password")
    }

    override suspend fun register(username: String, password: String): Token {
        if (users.filter{ it.username == username }.size == 0) {
            return Token("token")
        }
        else
            throw Exception("Username already exists")
    }
}*/

class RealLoginService(
    private val httpClient: OkHttpClient,
    private val jsonEncoder: Gson,
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

) : LoginService {

//implement using firebase related stuff
    override suspend fun login(username: String, password: String): Token {
      //check if username is present on users collection in firebase db

        db.apply {
            collection("users").get().addOnSuccessListener { result ->
                for (document in result) {
                    if(document.data["username"] == username){
                        if(document.data["password"] == password){
                            Token("token")
                        }
                        else
                            throw Exception("Invalid username or password")
                    }
                }
            }
        }
return Token("token")
    }

    override suspend fun register(username: String, password: String): Token {
        //check if username is present on users collection in firebase db
        db.apply {
            collection("users").get().addOnSuccessListener { result ->
                for (document in result) {
                    if(document.data["username"] == username){
                        throw Exception("Username already exists")
                    }
                    else{
                        //add user to users collection
                        val user = hashMapOf(
                            "username" to username,
                            "password" to password
                        )
                        db.collection("users").document(username).set(user)
                    }
                }
            }
        }
        return Token("token")
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