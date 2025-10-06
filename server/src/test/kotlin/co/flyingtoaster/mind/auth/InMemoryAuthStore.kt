package co.flyingtoaster.mind.auth

class InMemoryAuthStore<T : AuthTokenModel> : AuthStore<T> {

    private var authResponse: T? = null

    override fun getAuthResponse(): T? {
        return authResponse
    }

    override fun setAuthResponse(authResponse: T) {
        this.authResponse = authResponse
    }
}
