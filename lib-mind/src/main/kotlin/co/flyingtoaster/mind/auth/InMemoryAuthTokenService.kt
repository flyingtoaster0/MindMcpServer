package co.flyingtoaster.mind.auth

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("noredis")
class InMemoryAuthTokenService(
    private val objectMapper: ObjectMapper
) : InMemoryAuthStore<MindAuthTokenModel>() {

    private var tokenJson: String? = null

    override fun setAuthResponse(authResponse: MindAuthTokenModel) {
        tokenJson = objectMapper.writeValueAsString(authResponse)
    }

    override fun getAuthResponse(): MindAuthTokenModel? {
        return tokenJson?.let { objectMapper.readValue(it, MindAuthTokenModel::class.java) }
    }
}
