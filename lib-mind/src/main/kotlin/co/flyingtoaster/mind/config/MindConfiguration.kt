package co.flyingtoaster.mind.config

import co.flyingtoaster.mind.auth.MindAuthenticator
import co.flyingtoaster.mind.service.MindApiAuthService
import co.flyingtoaster.mind.service.MindApiService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Configuration
@ComponentScan("co.flyingtoaster.mind")
class MindConfiguration {

    @Bean
    fun mindApiAuthService(
        @Value("\${mind.base_url}") baseUrl: String,
        mindRetrofit: Retrofit
    ): MindApiAuthService {
        val normalizedUrl = normalizeBaseUrl(baseUrl)
        val objectMapper = createObjectMapper()
        val retrofit = Retrofit.Builder()
            .baseUrl(normalizedUrl)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()

        return retrofit.create(MindApiAuthService::class.java)
    }

    @Bean
    fun mindApiService(mindRetrofit: Retrofit): MindApiService {
        return mindRetrofit.create(MindApiService::class.java)
    }

    @Bean
    fun mindRetrofit(
        @Value("\${mind.base_url}") baseUrl: String,
        mindAuthenticator: MindAuthenticator
    ): Retrofit {
        val normalizedUrl = normalizeBaseUrl(baseUrl)
        val objectMapper = createObjectMapper()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(mindAuthenticator)
            .authenticator(mindAuthenticator)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(normalizedUrl)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()

        return retrofit
    }

    private fun normalizeBaseUrl(baseUrl: String): String {
        val trimmedUrl = baseUrl.trimEnd('/')
        return "$trimmedUrl/api/"
    }

    private fun createObjectMapper(): ObjectMapper {
        // TODO: Check that this is still needed
        return ObjectMapper().apply {
            registerModule(com.fasterxml.jackson.module.kotlin.KotlinModule.Builder().build())
            propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }
}