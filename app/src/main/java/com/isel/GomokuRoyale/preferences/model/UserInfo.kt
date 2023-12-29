package com.isel.GomokuRoyale.preferences.model

import model.openingrule
import model.toOpeningRule
import model.toVariante
import model.variantes

class UserInfo(val variante: String, val openingrule: String) {
    init {
        require(validateUserInfoParts(variante, openingrule))
    }
}
fun userInfoOrNull(variante: String, openingrule: String): UserInfo? =
    if (validateUserInfoParts(variante, openingrule))
        UserInfo(variante, openingrule)
    else
        null

fun validateUserInfoParts(variante: String, openingrule: String) =
    variante.isNotBlank() && openingrule.isNotBlank() && (variante.toVariante() == variantes.NORMAL || variante.toVariante() == variantes.OMOK)
            && (openingrule.toOpeningRule() == model.openingrule.PRO || openingrule.toOpeningRule() == model.openingrule.LONGPRO)