package co.flyingtoaster.mind.service.model

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateNotificationServiceRequest(
    @JsonProperty("title")
    val title: String,

    @JsonProperty("apprise_url")
    val appriseUrl: String
)