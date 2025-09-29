package co.flyingtoaster.mind.service

import java.time.LocalDateTime
import java.time.ZonedDateTime

interface MindService {

    fun createReminder(title: String, message: String?, localDateTime: LocalDateTime)
}