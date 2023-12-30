package com.isel.GomokuRoyale.preferences

import android.content.Context
import com.isel.GomokuRoyale.preferences.model.UserInfo

class UserInfoSharedPrefs(private val context: Context) {

    private val userVarianteKey = "Variante"
    private val userOpeningRuleKey = "OpeningRule"
    private val userTitleKey = "Title"

        private val prefs by lazy {
        context.getSharedPreferences("UserInfoPrefs", Context.MODE_PRIVATE)
    }


    var userInfo: UserInfo?
        get() {
            val savedVariante = prefs.getString(userVarianteKey, null)
            val savedOpeningRule = prefs.getString(userOpeningRuleKey,null)
            val savedTitle = prefs.getString(userOpeningRuleKey,null)
            return if (savedVariante != null && savedOpeningRule != null && savedTitle != null)
                UserInfo(savedVariante, savedOpeningRule,savedTitle)
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