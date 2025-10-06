package co.flyingtoaster.foundry.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Base64Test {

    @Test
    fun `encodes string`() {
        val javaDecoder = java.util.Base64.getDecoder()

        val encodedString = Base64.encode(TEST_STRING)

        val decodedString = String(javaDecoder.decode(encodedString))
        assertThat(decodedString).isEqualTo(TEST_STRING)
    }

    @Test
    fun `decodes string`() {
        val javaEncoder = java.util.Base64.getEncoder()
        val encodedString = javaEncoder.encodeToString(TEST_STRING.toByteArray())

        val decodedString = Base64.decode(encodedString)

        assertThat(decodedString).isEqualTo(TEST_STRING)
    }

    private companion object {
        const val TEST_STRING = "TEST"
    }
}