package co.flyingtoaster.mind.service

import co.flyingtoaster.mind.exception.MindException
import co.flyingtoaster.mind.service.model.CreateReminderRequest
import org.springframework.stereotype.Service
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class MindServiceImpl(
    private val mindApiService: MindApiService
) : MindService {

    override fun createReminder(title: String, message: String?, localDateTime: LocalDateTime) {
        val epochSeconds = localDateTime.toEpochSecond(ZoneOffset.UTC)
        val request = CreateReminderRequest(
            title = title,
            time = epochSeconds,
            notificationServices = listOf(1),
            text = message
        )

        try {
            val response = mindApiService.createReminder(request).execute()
            if (!response.isSuccessful) {
                throw MindException("Failed to create reminder: HTTP ${response.code()}\n$request")
            }
        } catch (e: IOException) {
            throw MindException("Failed to communicate with MIND service", e)
        }
    }
}