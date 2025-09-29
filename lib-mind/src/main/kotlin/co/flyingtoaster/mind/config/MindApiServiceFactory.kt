package co.flyingtoaster.mind.config

import co.flyingtoaster.mind.auth.MindAuthTokenModel
import co.flyingtoaster.mind.auth.RetrofitAuthenticator
import co.flyingtoaster.mind.service.MindApiAuthService
import co.flyingtoaster.mind.service.MindApiService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

internal class MindApiServiceFactory {

    private fun createObjectMapper(): ObjectMapper {
        // TODO: Check that this is still needed
        return ObjectMapper().apply {
            registerModule(com.fasterxml.jackson.module.kotlin.KotlinModule.Builder().build())
            propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }

    fun createMindApiAuthService(baseUrl: String): MindApiAuthService {
        val objectMapper = createObjectMapper()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()

        return retrofit.create(MindApiAuthService::class.java)
    }

    fun createMindApiService(baseUrl: String, authenticator: RetrofitAuthenticator<MindAuthTokenModel>): MindApiService {
        val objectMapper = createObjectMapper()
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authenticator)
            .authenticator(authenticator)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()

        return retrofit.create(MindApiService::class.java)
    }
}