package co.flyingtoaster.foundry.util

import java.util.Base64

class Base64 {
    companion object {
        fun encode(input: String) : String {
            return Base64.getEncoder().encodeToString(input.toByteArray())
        }

        fun decode(input: String) : String {
            return String(Base64.getDecoder().decode(input))
        }
    }
}
