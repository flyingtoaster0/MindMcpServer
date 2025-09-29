package co.flyingtoaster.mind.auth

interface AuthStore<T : AuthTokenModel> {
    fun getAuthResponse(): T?
    fun setAuthResponse(authResponse: T)
}