package co.flyingtoaster.mind.mcp.tools

import co.flyingtoaster.foundry.util.DateTimeProvider
import co.flyingtoaster.mind.exception.MindException
import co.flyingtoaster.mind.service.MindService
import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Service
class ReminderToolService(
    private val mindService: MindService,
    private val dateTimeProvider: DateTimeProvider,
    @Value("\${mind.timezone}") private val timezoneId: String
) {

    @Tool(description = "Create a reminder at a specific date and time. Can be called with just a time if the user provides only a time - the missing title will be requested in a follow-up. Can also be called with just the title if the user provides only a description - the missing date/time will be requested in a follow-up.")
    fun setReminderAt(
        @ToolParam(description = "The title of the reminder")
        title: String? = null,

        @ToolParam(description = "ISO 8601 formatted date (e.g., 2025-01-15)")
        date: String? = null,

        @ToolParam(description = "ISO 8601 formatted time (e.g., 10:30:00 or 10:30)")
        time: String? = null,

        @ToolParam(description = "Optional message/description for the reminder")
        message: String? = null
    ): CreateReminderResponse {

        if (title.isNullOrBlank()) {
            return CreateReminderResponse.Question(CreateReminderResponse.MissingInfo.MISSING_REMINDER_TEXT)
        }

        if (date.isNullOrBlank() || time.isNullOrBlank()) {
            return CreateReminderResponse.Question(CreateReminderResponse.MissingInfo.MISSING_DATE_TIME)
        }

        val localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
        val localTime = LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME)
        val localDateTime = LocalDateTime.of(localDate, localTime)

        return createReminder(title, message, localDateTime)
    }

    @Tool(description = "Create a reminder in the future relative to now (e.g., 'in 10 minutes', 'in 2 hours', 'in 3 days', 'in an hour and a half'). Can be called with just a time if the user provides only a time - the missing title will be requested in a follow-up. Can also be called with just the title if the user provides only a description - the missing time will be requested in a follow-up.")
    fun setReminderIn(
        @ToolParam(description = "The title of the reminder")
        title: String? = null,

        @ToolParam(description = "Number of minutes in the future for the reminder. 0 If not used.")
        minutes: Long? = null,

        @ToolParam(description = "Number of hours in the future for the reminder. 0 If not used.")
        hours: Long? = null,

        @ToolParam(description = "Number of days in the future for the reminder. 0 If not used.")
        days: Long? = null,

        @ToolParam(description = "Optional message/description for the reminder")
        message: String? = null
    ): CreateReminderResponse {

        if (title.isNullOrBlank()) {
            return CreateReminderResponse.Question(CreateReminderResponse.MissingInfo.MISSING_REMINDER_TEXT)
        }

        if (minutes == null && hours == null && days == null) {
            return CreateReminderResponse.Question(CreateReminderResponse.MissingInfo.MISSING_DATE_TIME)
        }

        val localDateTime = dateTimeProvider.getNow(timezoneId)
            .plusMinutes(minutes ?: 0)
            .plusHours(hours ?: 0)
            .plusDays(days ?: 0)

        return createReminder(title, message, localDateTime)
    }

    private fun createReminder(title: String, message: String?, localDateTime: LocalDateTime): CreateReminderResponse {
        try {
            mindService.createReminder(
                title = title,
                message = message,
                localDateTime = localDateTime
            )
        } catch (e: Exception) {
            return CreateReminderResponse.Error(e.message ?: e.toString())
        }

        return CreateReminderResponse.Success(
            reminder = title,
            reminderDateTime = localDateTime
        )
    }
}