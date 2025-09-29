package com.something.foundry.util

import co.flyingtoaster.foundry.util.DateTimeProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import java.time.Instant
import java.time.ZoneId

internal class DateTimeProviderTest {
    lateinit var subject: DateTimeProvider

    @BeforeEach
    fun setup() {
        subject = DateTimeProvider()
    }

    @RepeatedTest(10)
    fun `getNow returns a ZonedDateTime in UTC`() {
        val expectedNow = Instant.now()

        val now = subject.getNow()

        assertThat(now.zone).isEqualTo(ZoneId.of("UTC"))
        assertThat(now.toInstant().epochSecond).isGreaterThanOrEqualTo(expectedNow.epochSecond)
    }
}