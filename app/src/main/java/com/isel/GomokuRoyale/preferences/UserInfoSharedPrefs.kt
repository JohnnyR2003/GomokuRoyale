package com.isel.GomokuRoyale.preferences

import android.content.Context
import com.isel.GomokuRoyale.preferences.model.UserInfo
import com.isel.GomokuRoyale.preferences.model.UserInfoRepository

class UserInfoSharedPrefs(private val context: Context): UserInfoRepository {

    private val userUsernameKey = "Username"
    private val userStatusKey = "Status"

    private val prefs by lazy {
        context.getSharedPreferences("UserInfoPrefs", Context.MODE_PRIVATE)
    }

    override var userInfo: UserInfo?
        get() {
            val savedUsername = prefs.getString(userUsernameKey, null)
            val savedBearer = prefs.getString(userStatusKey,null)
            return if (savedUsername != null && savedBearer != null)
                UserInfo(savedUsername, savedBearer)
            else
                null
        }

        set(value) {
            if (value == null)
                prefs.edit()
                    .remove(userUsernameKey)
                    .remove(userStatusKey)
                    .apply()
            else
                prefs.edit()
                    .putString(userUsernameKey, value.username)
                    .putString(userStatusKey, value.status)
                    .apply()
        }



}