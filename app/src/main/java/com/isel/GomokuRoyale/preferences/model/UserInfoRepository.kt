package com.isel.GomokuRoyale.preferences.model

interface UserInfoRepository {
    //var userInfo: UserInfo?
    /**
     * Gets the user info if it exists, null otherwise.
     */
    suspend fun getUserInfo(): UserInfo?

    /**
     * Updates the user info.
     */
    suspend fun updateUserInfo(userInfo: UserInfo)


}

