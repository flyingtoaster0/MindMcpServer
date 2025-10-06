package co.flyingtoaster.mind.auth

import co.flyingtoaster.foundry.util.DateTimeProvider
import okhttp3.*
import org.slf4j.LoggerFactory

abstract class RetrofitAuthenticator<T : AuthTokenModel>(
    protected val authStore: AuthStore<T>,
    protected val dateTimeProvider: DateTimeProvider
) : Interceptor, Authenticator {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val currentAuthTokenModel = authStore.getAuthResponse()

        val accessToken: String
        val now = dateTimeProvider.getCurrentTimeMillis()
        val needsNewToken = currentAuthTokenModel == null || currentAuthTokenModel.isExpired(now)

        accessToken = if (needsNewToken) {
            val newAuthTokenModel = fetchAndCacheNewAuthTokenModel()
            newAuthTokenModel.token
        } else {
            currentAuthTokenModel.token
        }

        val url = request.url.newBuilder()
            .addQueryParameter("api_key", accessToken)
            .build()

        val authenticatedRequest = request.newBuilder()
            .url(url)
            .build()

        return chain.proceed(authenticatedRequest)
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        val responseCount = countPriorResponses(response)
        if (responseCount > MAX_AUTH_RETRIES) {
            logger.error(
                "{} auth failed after {} retries. Something is likely wrong with our credentials.",
                this::class.simpleName,
                MAX_AUTH_RETRIES
            )
            return null
        }

        val authTokenModel = fetchAndCacheNewAuthTokenModel()

        val url = response.request.url.newBuilder()
            .addQueryParameter("api_key", authTokenModel.token)
            .build()

        return response.request.newBuilder()
            .url(url)
            .build()
    }

    private fun countPriorResponses(response: Response): Int {
        var count = 1
        var responseToCount: Response? = response
        while (responseToCount?.priorResponse.also { responseToCount = it } != null) {
            count++
        }
        return count
    }

    private fun fetchAndCacheNewAuthTokenModel(): T {
        val authTokenModel = fetchNewAuthToken()
        authStore.setAuthResponse(authTokenModel)
        return authTokenModel
    }

    protected abstract fun fetchNewAuthToken(): T

    companion object {
        private const val MAX_AUTH_RETRIES = 3
    }
}