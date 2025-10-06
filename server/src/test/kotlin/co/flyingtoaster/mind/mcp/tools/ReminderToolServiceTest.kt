package co.flyingtoaster.mind.mcp.tools

import co.flyingtoaster.foundry.util.DateTimeProvider
import co.flyingtoaster.mind.auth.AuthStore
import co.flyingtoaster.mind.auth.FakeMindAuthenticator
import co.flyingtoaster.mind.auth.InMemoryAuthStore
import co.flyingtoaster.mind.auth.MindAuthTokenModel
import co.flyingtoaster.mind.mcp.tools.CreateReminderResponse.MissingInfo
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.time.LocalDateTime

@SpringBootTest
@TestPropertySource(
    properties = [
        "mind.username=user",
        "mind.password=pass",
        "mind.base_url=http://localhost:8080",
        "mind.timezone=America/Toronto",
        "spring.main.allow-bean-definition-overriding=true"
    ]
)
internal class ReminderToolServiceTest {

    private val TIMEZONE_ID = "America/Toronto"
    private val TITLE = "Drink some water!"
    private val MESSAGE = "Stay hydrated!"
    private val NOW = LocalDateTime.of(2024, 12, 15, 14, 30, 0)

    @MockitoBean
    private lateinit var dateTimeProvider: DateTimeProvider

    @Autowired
    private lateinit var subject: ReminderToolService

    @Autowired
    private lateinit var mockWebServer: MockWebServer

    @BeforeEach
    fun setup() {
        `when`(dateTimeProvider.getNow(TIMEZONE_ID)).thenReturn(NOW)
    }

    @Test
    fun `when setReminderAt is called with all parameters then Success is returned`() {
        mockWebServer.enqueue(MockResponse().setResponseCode(200))

        val result = subject.setReminderAt(
            title = TITLE,
            date = "2024-12-20",
            time = "15:00:00",
            message = MESSAGE
        )

        assertThat(result).isInstanceOf(CreateReminderResponse.Success::class.java)
        val success = result as CreateReminderResponse.Success
        assertThat(success.reminder).isEqualTo(TITLE)
        assertThat(success.reminderDateTime).isEqualTo(LocalDateTime.of(2024, 12, 20, 15, 0, 0))
    }

    @Test
    fun `when setReminderAt is called without title then Question with MISSING_REMINDER_TEXT is returned`() {
        val result = subject.setReminderAt(
            title = null,
            date = "2024-12-20",
            time = "15:00:00",
            message = MESSAGE
        )

        assertThat(result).isInstanceOf(CreateReminderResponse.Question::class.java)
        val question = result as CreateReminderResponse.Question
        assertThat(question.missingInfo).isEqualTo(MissingInfo.MISSING_REMINDER_TEXT)
    }

    @Test
    fun `when setReminderAt is called with blank title then Question with MISSING_REMINDER_TEXT is returned`() {
        val result = subject.setReminderAt(
            title = "   ",
            date = "2024-12-20",
            time = "15:00:00",
            message = MESSAGE
        )

        assertThat(result).isInstanceOf(CreateReminderResponse.Question::class.java)
        val question = result as CreateReminderResponse.Question
        assertThat(question.missingInfo).isEqualTo(MissingInfo.MISSING_REMINDER_TEXT)
    }

    @Test
    fun `when setReminderAt is called without date then Question with MISSING_DATE_TIME is returned`() {
        val result = subject.setReminderAt(
            title = TITLE,
            date = null,
            time = "15:00:00",
            message = MESSAGE
        )

        assertThat(result).isInstanceOf(CreateReminderResponse.Question::class.java)
        val question = result as CreateReminderResponse.Question
        assertThat(question.missingInfo).isEqualTo(MissingInfo.MISSING_DATE_TIME)
    }

    @Test
    fun `when setReminderAt is called without time then Question with MISSING_DATE_TIME is returned`() {
        val result = subject.setReminderAt(
            title = TITLE,
            date = "2024-12-20",
            time = null,
            message = MESSAGE
        )

        assertThat(result).isInstanceOf(CreateReminderResponse.Question::class.java)
        val question = result as CreateReminderResponse.Question
        assertThat(question.missingInfo).isEqualTo(MissingInfo.MISSING_DATE_TIME)
    }

    @Test
    fun `when setReminderAt is called without message then Success is returned with null message`() {
        mockWebServer.enqueue(MockResponse().setResponseCode(200))

        val result = subject.setReminderAt(
            title = TITLE,
            date = "2024-12-20",
            time = "15:00:00",
            message = null
        )

        assertThat(result).isInstanceOf(CreateReminderResponse.Success::class.java)
    }

    @Test
    fun `when setReminderIn is called with all parameters then Success is returned`() {
        mockWebServer.enqueue(MockResponse().setResponseCode(200))

        val result = subject.setReminderIn(
            title = TITLE,
            minutes = 30,
            hours = 2,
            days = 1,
            message = MESSAGE
        )

        assertThat(result).isInstanceOf(CreateReminderResponse.Success::class.java)
        val success = result as CreateReminderResponse.Success
        assertThat(success.reminder).isEqualTo(TITLE)
        assertThat(success.reminderDateTime).isEqualTo(NOW.plusDays(1).plusHours(2).plusMinutes(30))
    }

    @Test
    fun `when setReminderIn is called with only minutes then Success is returned`() {
        mockWebServer.enqueue(MockResponse().setResponseCode(200))

        val result = subject.setReminderIn(
            title = TITLE,
            minutes = 30,
            message = MESSAGE
        )

        assertThat(result).isInstanceOf(CreateReminderResponse.Success::class.java)
        val success = result as CreateReminderResponse.Success
        assertThat(success.reminderDateTime).isEqualTo(NOW.plusMinutes(30))
    }

    @Test
    fun `when setReminderIn is called without title then Question with MISSING_REMINDER_TEXT is returned`() {
        val result = subject.setReminderIn(
            title = null,
            minutes = 30,
            message = MESSAGE
        )

        assertThat(result).isInstanceOf(CreateReminderResponse.Question::class.java)
        val question = result as CreateReminderResponse.Question
        assertThat(question.missingInfo).isEqualTo(MissingInfo.MISSING_REMINDER_TEXT)
    }

    @Test
    fun `when setReminderIn is called without time parameters then Question with MISSING_DATE_TIME is returned`() {
        val result = subject.setReminderIn(
            title = TITLE,
            message = MESSAGE
        )

        assertThat(result).isInstanceOf(CreateReminderResponse.Question::class.java)
        val question = result as CreateReminderResponse.Question
        assertThat(question.missingInfo).isEqualTo(MissingInfo.MISSING_DATE_TIME)
    }

    @TestConfiguration
    class ReminderToolServiceTestConfig {

        @Bean
        fun mockWebServer(): MockWebServer {
            val mockWebServer = MockWebServer()
            mockWebServer.start()
            return mockWebServer
        }

        @Bean
        fun authStore(): AuthStore<MindAuthTokenModel> {
            return InMemoryAuthStore()
        }

        @Bean
        @Primary
        fun fakeMindAuthenticator(
            authStore: AuthStore<MindAuthTokenModel>,
            dateTimeProvider: DateTimeProvider
        ): FakeMindAuthenticator {
            return FakeMindAuthenticator(authStore, dateTimeProvider)
        }

        @Bean
        @Primary
        fun mindRetrofit(fakeMindAuthenticator: FakeMindAuthenticator, mockWebServer: MockWebServer): Retrofit {
            val objectMapper = ObjectMapper().apply {
                registerModule(com.fasterxml.jackson.module.kotlin.KotlinModule.Builder().build())
                propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(fakeMindAuthenticator)
                .authenticator(fakeMindAuthenticator)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()

            return retrofit
        }
    }
}
