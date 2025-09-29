package co.flyingtoaster.mind.auth

interface AuthTokenModel {
    val token: String
    fun isExpired(currentTimeMillis: Long): Boolean
}