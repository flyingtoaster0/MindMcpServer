package co.flyingtoaster.mind.auth

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

@Service
internal class MindAuthTokenService(
    private val stringRedisTemplate: StringRedisTemplate,
    private val objectMapper: ObjectMapper
) : AuthStore<MindAuthTokenModel> {

    override fun setAuthResponse(authResponse: MindAuthTokenModel) {
        val tokenJson = objectMapper.writeValueAsString(authResponse)
        stringRedisTemplate.opsForValue().set(MIND_API_TOKEN_KEY, tokenJson)
    }

    override fun getAuthResponse(): MindAuthTokenModel? {
        val tokenJson = stringRedisTemplate.opsForValue().get(MIND_API_TOKEN_KEY)
        return tokenJson?.let { objectMapper.readValue(it, MindAuthTokenModel::class.java) }
    }

    companion object {
        private const val MIND_API_TOKEN_KEY = "MIND_API_TOKEN"
    }
}