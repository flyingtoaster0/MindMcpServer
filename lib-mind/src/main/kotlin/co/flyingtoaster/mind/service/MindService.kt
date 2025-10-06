package co.flyingtoaster.mind.service

import java.time.LocalDateTime

interface MindService {

    fun createReminder(title: String, message: String?, localDateTime: LocalDateTime)
}