package co.flyingtoaster.mind.service.model

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginRequest(
    @JsonProperty("username")
    val username: String,

    @JsonProperty("password")
    val password: String,

    @JsonProperty("mfa_code")
    val mfaCode: String? = null
)