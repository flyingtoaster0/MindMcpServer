package co.flyingtoaster.foundry.util

import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Component
class DateTimeProvider {
    fun getNow(zoneId: String = ZONE_ID): LocalDateTime {
        val now = Instant.ofEpochMilli(System.currentTimeMillis())
        return LocalDateTime.ofInstant(now, ZoneId.of(zoneId))
    }

    fun getCurrentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

    companion object {
        const val ZONE_ID = "UTC"
    }
}
