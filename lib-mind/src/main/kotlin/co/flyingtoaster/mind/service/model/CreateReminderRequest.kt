package co.flyingtoaster.mind.service.model

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateReminderRequest(
    @JsonProperty("title")
    val title: String,

    @JsonProperty("time")
    val time: Long,

    @JsonProperty("notification_services")
    val notificationServices: List<Int>,

    @JsonProperty("text")
    val text: String? = null
)