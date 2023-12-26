package com.isel.GomokuRoyale.lobby.domain

import com.isel.GomokuRoyale.preferences.model.UserInfo
import java.util.UUID

data class PlayerInfo(val info:UserInfo, val id: UUID = UUID.randomUUID())