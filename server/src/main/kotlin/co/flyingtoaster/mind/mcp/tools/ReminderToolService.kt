package co.flyingtoaster.mind.mcp.tools

import co.flyingtoaster.foundry.util.DateTimeProvider
import co.flyingtoaster.mind.service.MindService
import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Service
class ReminderToolService(
    private val mindService: MindService,
    private val dateTimeProvider: DateTimeProvider
) {

    @Tool(description = "Create a reminder at a specific date and time")
    fun setReminderAt(
        @ToolParam(required = true, description = "The title of the reminder")
        title: String,

        @ToolParam(required = true, description = "ISO 8601 formatted date (e.g., 2025-01-15)")
        date: String,

        @ToolParam(required = true, description = "ISO 8601 formatted time (e.g., 10:30:00 or 10:30)")
        time: String,

        @ToolParam(description = "Optional message/description for the reminder")
        message: String? = null
    ): String {

        val localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
        val localTime = LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME)
        val localDateTime = LocalDateTime.of(localDate, localTime)

        return createReminder(title, message, localDateTime)
    }

    @Tool(description = "Create a reminder in the future relative to now (e.g., 'in 10 minutes', 'in 2 hours', 'in 3 days', 'in an hour and a half')")
    fun setReminderIn(
        @ToolParam(required = true, description = "The title of the reminder")
        title: String,

        @ToolParam(required = true, description = "Number of minutes in the future for the reminder. 0 If not used.")
        minutes: Long,

        @ToolParam(required = true, description = "Number of hours in the future for the reminder. 0 If not used.")
        hours: Long,

        @ToolParam(required = true, description = "Number of days in the future for the reminder. 0 If not used.")
        days: Long,

        @ToolParam(description = "Optional message/description for the reminder")
        message: String? = null
    ): String {
        val localDateTime = dateTimeProvider.getNow()
            .plusMinutes(minutes)
            .plusHours(hours)
            .plusDays(days)

        return createReminder(title, message, localDateTime)
    }

    private fun createReminder(title: String, message: String?, localDateTime: LocalDateTime): String {
        mindService.createReminder(
            title = title,
            message = message,
            localDateTime = localDateTime
        )

        return "Reminder created successfully: $title at $localDateTime"
    }
}