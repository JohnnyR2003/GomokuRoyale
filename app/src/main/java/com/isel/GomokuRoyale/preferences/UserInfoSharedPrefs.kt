package com.isel.GomokuRoyale.preferences

import android.content.Context
import com.isel.GomokuRoyale.preferences.model.UserInfo
import com.isel.GomokuRoyale.preferences.model.UserInfoRepository

class UserInfoSharedPrefs(private val context: Context) {

    private val userVarianteKey = "Variante"
    private val userOpeningRuleKey = "OpeningRule"

        private val prefs by lazy {
        context.getSharedPreferences("UserInfoPrefs", Context.MODE_PRIVATE)
    }


    var userInfo: UserInfo?
        get() {
            val savedUsername = prefs.getString(userVarianteKey, null)
            val savedBearer = prefs.getString(userOpeningRuleKey,null)
            return if (savedUsername != null && savedBearer != null)
                UserInfo(savedUsername, savedBearer)
            else
                null
        }

        set(value) {
            if (value == null)
                prefs.edit()
                    .remove(userVarianteKey)
                    .remove(userOpeningRuleKey)
                    .apply()
            else
                prefs.edit()
                    .putString(userVarianteKey, value.variante)
                    .putString(userOpeningRuleKey, value.openingrule)
                    .apply()
        }



}