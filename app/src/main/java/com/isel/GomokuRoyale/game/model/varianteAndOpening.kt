package model

enum class variantes {
    NORMAL,
    OMOK,
}
fun String.toVariante() = when (this) {
    "NORMAL" -> variantes.NORMAL
    "OMOK" -> variantes.OMOK
    else -> throw IllegalArgumentException("Invalid variant.")
}
enum class openingrule {
    LONGPRO,
    PRO,

}
fun String.toOpeningRule() = when (this) {
    "LONGPRO" -> openingrule.LONGPRO
    "PRO" -> openingrule.PRO
    else -> throw IllegalArgumentException("Invalid opening rule.")
}
