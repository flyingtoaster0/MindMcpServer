package co.flyingtoaster.mind.service

import co.flyingtoaster.mind.service.model.*
import retrofit2.Call
import retrofit2.http.*

interface MindApiService {

    @GET("reminders")
    fun getReminders(
        @Query("sort_by") sortBy: String? = null
    ): Call<List<ReminderResponse>>

    @POST("reminders")
    fun createReminder(
        @Body request: CreateReminderRequest
    ): Call<Void>

    @GET("reminders/{r_id}")
    fun getReminder(
        @Path("r_id") reminderId: Int
    ): Call<ReminderResponse>

    @PUT("reminders/{r_id}")
    fun updateReminder(
        @Path("r_id") reminderId: Int,
        @Body request: UpdateReminderRequest
    ): Call<Void>

    @DELETE("reminders/{r_id}")
    fun deleteReminder(
        @Path("r_id") reminderId: Int
    ): Call<Void>

    @GET("notificationservices")
    fun getNotificationServices(): Call<List<NotificationServiceResponse>>

    @POST("notificationservices")
    fun createNotificationService(
        @Body request: CreateNotificationServiceRequest
    ): Call<Void>

    @POST("notificationservices/test")
    fun testNotificationService(
        @Body request: TestNotificationServiceRequest
    ): Call<Void>

    @GET("notificationservices/{n_id}")
    fun getNotificationService(
        @Path("n_id") notificationServiceId: Int
    ): Call<NotificationServiceResponse>

    @PUT("notificationservices/{n_id}")
    fun updateNotificationService(
        @Path("n_id") notificationServiceId: Int,
        @Body request: UpdateNotificationServiceRequest
    ): Call<Void>

    @DELETE("notificationservices/{n_id}")
    fun deleteNotificationService(
        @Path("n_id") notificationServiceId: Int
    ): Call<Void>
}