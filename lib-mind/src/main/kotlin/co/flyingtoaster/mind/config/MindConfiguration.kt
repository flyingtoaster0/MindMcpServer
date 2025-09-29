package co.flyingtoaster.mind.config

import co.flyingtoaster.mind.auth.MindAuthenticator
import co.flyingtoaster.mind.service.MindApiAuthService
import co.flyingtoaster.mind.service.MindApiService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan("co.flyingtoaster.mind")
class MindConfiguration {

    @Bean
    fun mindApiAuthService(@Value("\${mind.base_url}") baseUrl: String): MindApiAuthService {
        val factory = MindApiServiceFactory()
        return factory.createMindApiAuthService(baseUrl)
    }

    @Bean
    fun mindApiService(
        @Value("\${mind.base_url}") baseUrl: String,
        mindAuthenticator: MindAuthenticator
    ): MindApiService {
        val factory = MindApiServiceFactory()
        return factory.createMindApiService(baseUrl, mindAuthenticator)
    }
}