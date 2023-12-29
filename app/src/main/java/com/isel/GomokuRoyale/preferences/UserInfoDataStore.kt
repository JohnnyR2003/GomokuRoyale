package com.isel.GomokuRoyale.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.isel.GomokuRoyale.preferences.model.UserInfo
import com.isel.GomokuRoyale.preferences.model.UserInfoRepository
import kotlinx.coroutines.flow.first

private const val USER_NICK_KEY = "Nick"
private const val USER_MOTTO_KEY = "Motto"

/**
 * A user information repository implementation supported in DataStore, the
 * modern alternative to SharedPreferences.
 */
class UserInfoDataStore(private val store: DataStore<Preferences>) : UserInfoRepository {

    private val varianteKey = stringPreferencesKey(USER_NICK_KEY)
    private val openingRuleKey = stringPreferencesKey(USER_MOTTO_KEY)

    override suspend fun getUserInfo(): UserInfo? {
        val preferences = store.data.first()
        val variante = preferences[varianteKey]
        val openingRule = preferences[openingRuleKey]
        return if (variante != null && openingRule != null) UserInfo(variante, openingRule) else null
    }

    override suspend fun updateUserInfo(userInfo: UserInfo) {
        store.edit { preferences ->
            preferences[varianteKey] = userInfo.variante
            userInfo.openingrule.let {
                preferences[openingRuleKey] = it
            } ?: preferences.remove(openingRuleKey)
        }
    }
}