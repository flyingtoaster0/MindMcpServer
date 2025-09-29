package co.flyingtoaster.mind.service.model

import com.fasterxml.jackson.annotation.JsonProperty

data class NotificationServiceResponse(
    @JsonProperty("n_id")
    val notificationServiceId: Int,

    @JsonProperty("title")
    val title: String,

    @JsonProperty("apprise_url")
    val appriseUrl: String
)