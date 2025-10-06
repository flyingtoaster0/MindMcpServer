package co.flyingtoaster.mind.service

import co.flyingtoaster.mind.exception.MindException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.time.LocalDateTime

internal class MindServiceImplTest {

    private val TIMEZONE_ID = "America/Toronto"
    private val TITLE = "Meeting Reminder"
    private val MESSAGE = "There's a meeting!"
    private val NOW = LocalDateTime.of(2024, 12, 15, 14, 30, 0)

    private lateinit var mockWebServer: MockWebServer
    private lateinit var mindApiService: MindApiService
    private lateinit var subject: MindServiceImpl

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val objectMapper = ObjectMapper().apply {
            registerModule(com.fasterxml.jackson.module.kotlin.KotlinModule.Builder().build())
            propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()

        mindApiService = retrofit.create(MindApiService::class.java)
        subject = MindServiceImpl(mindApiService, TIMEZONE_ID)
    }

    @AfterEach
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `when createReminder is called with valid data then reminder is created successfully`() {
        mockWebServer.enqueue(MockResponse().setResponseCode(200))

        subject.createReminder(TITLE, MESSAGE, NOW)

        val request = mockWebServer.takeRequest()
        val requestBody = request.body.readUtf8()
        assertThat(request.method).isEqualTo("POST")
        assertThat(request.path).isEqualTo("/reminders")
        assertThat(requestBody).contains("\"title\":\"$TITLE\"")
        assertThat(requestBody).contains("\"text\":\"$MESSAGE\"")
    }

    @Test
    fun `when createReminder is called without message then reminder is created with null text`() {
        mockWebServer.enqueue(MockResponse().setResponseCode(200))

        subject.createReminder(TITLE, null, NOW)

        val request = mockWebServer.takeRequest()
        assertThat(request.method).isEqualTo("POST")
        assertThat(request.path).isEqualTo("/reminders")
    }

    @Test
    fun `when API returns non-successful response then MindException is thrown`() {
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        assertThatThrownBy {
            subject.createReminder(TITLE, MESSAGE, NOW)
        }.isInstanceOf(MindException::class.java)
            .hasMessageContaining("Failed to create reminder: HTTP 500")
    }

    @Test
    fun `when API returns 404 then MindException is thrown`() {
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        assertThatThrownBy {
            subject.createReminder(TITLE, MESSAGE, NOW)
        }.isInstanceOf(MindException::class.java)
            .hasMessageContaining("Failed to create reminder: HTTP 404")
    }

    @Test
    fun `when IOException occurs then MindException is thrown`() {
        mockWebServer.shutdown()

        assertThatThrownBy {
            subject.createReminder(TITLE, MESSAGE, NOW)
        }
            .isInstanceOf(MindException::class.java)
            .hasMessageContaining("Failed to communicate with MIND service")
    }
}
