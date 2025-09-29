package co.flyingtoaster.mind.service.model

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginResponse(
    @JsonProperty("error")
    val error: String?,

    @JsonProperty("result")
    val result: LoginResult
)

data class LoginResult(
    @JsonProperty("admin")
    val admin: Boolean,

    @JsonProperty("api_key")
    val apiKey: String,

    @JsonProperty("expires")
    val expires: Double
)