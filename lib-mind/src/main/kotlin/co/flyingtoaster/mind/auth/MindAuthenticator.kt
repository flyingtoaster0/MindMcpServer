package co.flyingtoaster.mind.auth

import co.flyingtoaster.foundry.util.DateTimeProvider
import co.flyingtoaster.mind.service.MindApiAuthService
import co.flyingtoaster.mind.service.model.LoginRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class MindAuthenticator(
    authStore: AuthStore<MindAuthTokenModel>,
    dateTimeProvider: DateTimeProvider,
    private val mindApiAuthService: MindApiAuthService,
    @Value("\${mind.username}") private val username: String,
    @Value("\${mind.password}") private val password: String
) : RetrofitAuthenticator<MindAuthTokenModel>(authStore, dateTimeProvider) {

    override fun fetchNewAuthToken(): MindAuthTokenModel {
        val loginRequest = LoginRequest(username, password)
        val response = mindApiAuthService.login(loginRequest).execute()

        if (!response.isSuccessful || response.body() == null) {
            throw IOException("Failed to authenticate with MIND service: HTTP ${response.code()}")
        }

        val loginResponse = response.body()!!

        if (loginResponse.error != null) {
            throw IOException("MIND authentication error: ${loginResponse.error}")
        }

        val loginResult = loginResponse.result
        val expiresAtMillis = (loginResult.expires * 1000).toLong()

        return MindAuthTokenModel(
            token = loginResult.apiKey,
            expiresAt = expiresAtMillis
        )
    }
}