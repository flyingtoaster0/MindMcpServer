package co.flyingtoaster.mind.service.model

import com.fasterxml.jackson.annotation.JsonProperty

data class TestNotificationServiceRequest(
    @JsonProperty("apprise_url")
    val appriseUrl: String
)