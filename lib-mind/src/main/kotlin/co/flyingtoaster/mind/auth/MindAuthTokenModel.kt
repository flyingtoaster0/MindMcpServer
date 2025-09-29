package co.flyingtoaster.mind.auth

data class MindAuthTokenModel(
    override val token: String,
    val expiresAt: Long,
) : AuthTokenModel {
    override fun isExpired(currentTimeMillis: Long): Boolean {
        return currentTimeMillis > expiresAt
    }
}