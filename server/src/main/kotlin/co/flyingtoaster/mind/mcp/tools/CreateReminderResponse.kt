package co.flyingtoaster.mind.mcp.tools

import java.time.LocalDateTime

sealed class CreateReminderResponse {

    data class Success(
        val reminder: String,
        val reminderDateTime: LocalDateTime
    ) : CreateReminderResponse()

    data class Question(
        val missingInfo: MissingInfo
    ) : CreateReminderResponse()

    data class Error(
        val message: String
    ) : CreateReminderResponse()

    enum class MissingInfo {
        MISSING_DATE_TIME,
        MISSING_REMINDER_TEXT
    }
}
