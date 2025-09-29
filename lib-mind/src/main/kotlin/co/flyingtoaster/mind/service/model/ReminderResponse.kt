package co.flyingtoaster.mind.service.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ReminderResponse(
    @JsonProperty("r_id")
    val reminderId: Int,

    @JsonProperty("title")
    val title: String,

    @JsonProperty("time")
    val time: Double,

    @JsonProperty("notification_services")
    val notificationServices: List<Int>,

    @JsonProperty("text")
    val text: String?,

    @JsonProperty("repeat_quantity")
    val repeatQuantity: String?,

    @JsonProperty("repeat_interval")
    val repeatInterval: Int?,

    @JsonProperty("weekdays")
    val weekdays: List<Int>?,

    @JsonProperty("cron_schedule")
    val cronSchedule: String?,

    @JsonProperty("color")
    val color: String?,

    @JsonProperty("enabled")
    val enabled: Boolean
)