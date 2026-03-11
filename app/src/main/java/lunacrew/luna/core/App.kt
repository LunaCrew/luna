package lunacrew.luna.core

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lunacrew.luna.database.AppDatabase
import lunacrew.luna.util.devtools.SentryConfig
import lunacrew.luna.util.extensions.processLifecycleScope
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class App: Application() {
    @Inject
    lateinit var database: Provider<AppDatabase>

    @Inject
    lateinit var sentry: Provider<SentryConfig>

    override fun onCreate() {
        super.onCreate()
        processLifecycleScope.launch(Dispatchers.IO) {
            database.get()
            sentry.get()
        }
    }
}
