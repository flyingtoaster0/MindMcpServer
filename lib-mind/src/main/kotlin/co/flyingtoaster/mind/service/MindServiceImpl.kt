package co.flyingtoaster.mind.service

import co.flyingtoaster.mind.exception.MindException
import co.flyingtoaster.mind.service.model.CreateReminderRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Service
class MindServiceImpl(
    private val mindApiService: MindApiService,
    @Value("\${mind.timezone}") private val timezoneId: String
) : MindService {

    override fun createReminder(title: String, message: String?, localDateTime: LocalDateTime) {
        val zoneId = ZoneId.of(timezoneId)
        val zonedDateTime = localDateTime.atZone(zoneId)
        val offsetSeconds = zonedDateTime.offset.totalSeconds.toLong()
        val epochSeconds = zonedDateTime.toInstant().epochSecond - offsetSeconds
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