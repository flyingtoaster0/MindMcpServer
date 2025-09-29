package co.flyingtoaster.mind.service.model

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateNotificationServiceRequest(
    @JsonProperty("title")
    val title: String? = null,

    @JsonProperty("apprise_url")
    val appriseUrl: String? = null
)