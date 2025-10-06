package co.flyingtoaster.mind.auth

import co.flyingtoaster.foundry.util.DateTimeProvider

class FakeMindAuthenticator(
    authStore: AuthStore<MindAuthTokenModel>,
    dateTimeProvider: DateTimeProvider
) : RetrofitAuthenticator<MindAuthTokenModel>(authStore, dateTimeProvider) {

    private val FAKE_TOKEN = "fake-api-key-12345"
    private val EXPIRES_AT = 9999999999999L

    override fun fetchNewAuthToken(): MindAuthTokenModel {
        return MindAuthTokenModel(
            token = FAKE_TOKEN,
            expiresAt = EXPIRES_AT
        )
    }
}
