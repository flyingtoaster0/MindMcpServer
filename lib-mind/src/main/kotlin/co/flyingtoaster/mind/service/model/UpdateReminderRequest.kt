package co.flyingtoaster.mind.service.model

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateReminderRequest(
    @JsonProperty("title")
    val title: String? = null,

    @JsonProperty("time")
    val time: Double? = null,

    @JsonProperty("notification_services")
    val notificationServices: List<Int>? = null,

    @JsonProperty("text")
    val text: String? = null,

    @JsonProperty("repeat_quantity")
    val repeatQuantity: String? = null,

    @JsonProperty("repeat_interval")
    val repeatInterval: Int? = null,

    @JsonProperty("weekdays")
    val weekdays: List<Int>? = null,

    @JsonProperty("cron_schedule")
    val cronSchedule: String? = null,

    @JsonProperty("color")
    val color: String? = null,

    @JsonProperty("enabled")
    val enabled: Boolean? = null
)