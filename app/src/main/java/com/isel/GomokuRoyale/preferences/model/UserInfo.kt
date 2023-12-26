package com.isel.GomokuRoyale.preferences.model

class UserInfo(val username: String, val status: String) {
    init {
        require(validateUserInfoParts(username, status))
    }
}
fun userInfoOrNull(username: String, status: String): UserInfo? =
    if (validateUserInfoParts(username, status))
        UserInfo(username, status)
    else
        null

fun validateUserInfoParts(username: String, status: String) =
    (username.isNotBlank() && status.isNotBlank() && status != "null") ?: true