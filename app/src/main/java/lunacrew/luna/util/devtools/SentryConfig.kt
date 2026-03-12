package lunacrew.luna.util.devtools

import android.content.Context
import io.sentry.android.core.SentryAndroid
import lunacrew.luna.BuildConfig
import lunacrew.luna.core.App
import lunacrew.luna.util.constants.TableSettings
import javax.inject.Inject
import javax.inject.Provider

class SentryConfig(context: Context) {
    @Inject lateinit var settings: Provider<TableSettings>

    init {
        SentryAndroid.init(context) { options ->
            options.isEnabled = isEnabled()
            options.isDebug = BuildConfig.DEBUG
            options.dsn = BuildConfig.SENTRY_DSN
        }
    }


    private fun isEnabled(): Boolean =
        App().database.get().settingsDao()
            .findByName(settings.get().SENTRY_ENABLED)
            .value.toBoolean()
}
