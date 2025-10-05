package com.something.foundry.util

import co.flyingtoaster.foundry.util.DateTimeProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class DateTimeProviderTest {

    private val NOW = LocalDateTime.of(2024, 6, 15, 10, 30, 0)

    private lateinit var subject: DateTimeProvider

    @BeforeEach
    fun setup() {
        subject = DateTimeProvider()
    }

    @Test
    fun `getNow returns a LocalDateTime`() {
        val now = subject.getNow()

        assertThat(now).isNotNull()
        assertThat(now).isAfter(NOW)
    }
}